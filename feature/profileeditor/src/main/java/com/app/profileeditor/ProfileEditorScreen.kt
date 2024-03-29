package com.app.profileeditor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.profileeditor.uistate.ProfileEditUiEvent
import com.app.profileeditor.uistate.ProfileEditUiState
import com.app.profileeditor.uistate.ProfileNicknameValidUiState
import com.app.profileeditor.uistate.ProfileUiModel
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.permission.ImagePermissionHelper
import com.withpeace.withpeace.feature.profileeditor.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun ProfileEditorRoute(
    onShowSnackBar: (message: String) -> Unit,
    onClickBackButton: () -> Unit,
    onNavigateToGallery: () -> Unit,
    viewModel: ProfileEditorViewModel,
    onUpdateSuccess: () -> Unit,
) {
    var showAlertDialog by remember { mutableStateOf(false) }
    val profileUiState: ProfileEditUiState by viewModel.profileEditUiState.collectAsStateWithLifecycle()

    val profileInfo = when (profileUiState) {
        is ProfileEditUiState.Editing -> {
            val editing = profileUiState as ProfileEditUiState.Editing
            ProfileUiModel(nickname = editing.profileInfo.nickname, profileImage = editing.profileInfo.profileImage)
        }

        is ProfileEditUiState.NoChanges -> {
            viewModel.updateIsNicknameValidStatus(ProfileNicknameValidUiState.Valid)
            viewModel.baseProfileInfo
        }
    }
    if (showAlertDialog) {
        ModifySaveDialog(
            onClickSave = {
                showAlertDialog = false
                viewModel.updateProfile()
            },
            onClickExit = {
                showAlertDialog = false
                onClickBackButton()
            },
            onModifyDismissRequest = {
                showAlertDialog = false
            },
        )
    }

    LaunchedEffect(viewModel.profileEditUiEvent) {
        viewModel.profileEditUiEvent.collect {
            when (it) {
                ProfileEditUiEvent.ShowDuplicate -> {
                    viewModel.updateIsNicknameValidStatus(ProfileNicknameValidUiState.InValidDuplicated)
                }

                ProfileEditUiEvent.ShowDuplicateSnackBar -> {
                    onShowSnackBar("중복된 닉네임입니다.")
                }

                ProfileEditUiEvent.ShowFailure -> {
                    onShowSnackBar("서버와 통신 중 오류가 발생했습니다.")
                }

                ProfileEditUiEvent.ShowInvalidFormat -> {
                    viewModel.updateIsNicknameValidStatus(ProfileNicknameValidUiState.InValidFormat)
                }

                ProfileEditUiEvent.ShowUnchanged -> {
                    onShowSnackBar("수정사항이 없습니다.")
                }

                ProfileEditUiEvent.ShowUpdateSuccess -> {
                    onShowSnackBar("변경되었습니다.")
                    onUpdateSuccess()
                }

                ProfileEditUiEvent.ShowNicknameVerified -> {
                    viewModel.updateIsNicknameValidStatus(ProfileNicknameValidUiState.Valid)
                }
            }
        }
    }


    ProfileEditorScreen(
        profileInfo = ProfileUiModel(profileInfo.nickname, profileInfo.profileImage),
        onClickBackButton = {
            if (profileUiState is ProfileEditUiState.Editing) {
                showAlertDialog = true
            } else {
                onClickBackButton()
            }
        },
        onNavigateToGallery = onNavigateToGallery,
        onEditCompleted = {
            viewModel.updateProfile()
        },
        onNickNameChanged = viewModel::onNickNameChanged,
        onKeyBoardTimerEnd = {
            viewModel.verifyNickname()
        },
        nicknameValidStatus = viewModel.profileNicknameValidUiState.collectAsStateWithLifecycle().value,
    )

}

@Composable
fun ProfileEditorScreen(
    profileInfo: ProfileUiModel,
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onEditCompleted: () -> Unit,
    onNickNameChanged: (String) -> Unit,
    onKeyBoardTimerEnd: () -> Unit,
    nicknameValidStatus: ProfileNicknameValidUiState,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 0.dp),
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WithPeaceBackButtonTopAppBar(
                modifier = modifier,
                onClickBackButton = { onClickBackButton() },
                title = {
                    Text(
                        text = stringResource(R.string.edit_profile),
                        style = WithpeaceTheme.typography.title1,
                        color = WithpeaceTheme.colors.SystemBlack,
                    )
                },
            )
            Spacer(modifier = modifier.height(16.dp))
            ProfileImage(
                profileImage = profileInfo.profileImage,
                modifier = modifier,
                onNavigateToGallery = onNavigateToGallery,
            )
            Spacer(modifier = modifier.height(24.dp))
            Text(
                text = stringResource(R.string.nickname_policy),
                style = WithpeaceTheme.typography.caption,
                color = WithpeaceTheme.colors.SystemGray1,
            )
            Spacer(modifier = modifier.height(16.dp))
            NickNameTextField(
                nickname = profileInfo.nickname,
                onNickNameChanged = {
                    onNickNameChanged(it)
                },
                onKeyBoardTimerEnd = onKeyBoardTimerEnd,
                nicknameValidStatus = nicknameValidStatus,
            )

        }
        EditCompletedButton(onClick = { onEditCompleted() })
    }
}

@Composable
private fun ProfileImage(
    profileImage: String?,
    modifier: Modifier,
    onNavigateToGallery: () -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val imagePermissionHelper = remember { ImagePermissionHelper(context) }
    val launcher = imagePermissionHelper.getImageLauncher(
        onPermissionGranted = onNavigateToGallery,
        onPermissionDenied = { showDialog = true },
    )
    if (showDialog) {
        imagePermissionHelper.ImagePermissionDialog { showDialog = false }
    }
    val imageModifier = modifier
        .size(120.dp)
        .clip(CircleShape)
    Row(
        modifier = modifier.wrapContentSize(Alignment.Center),
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier.clickable {
                imagePermissionHelper.onCheckSelfImagePermission(
                    onPermissionGranted = onNavigateToGallery,
                    onPermissionDenied = {
                        imagePermissionHelper.requestPermissionDialog(launcher)
                    },
                )
            },
        ) {
            GlideImage(
                modifier = imageModifier,
                imageModel = { profileImage },
                failure = {
                    Image(
                        painterResource(id = R.drawable.ic_default_profile),
                        modifier = imageModifier,
                        contentDescription = "",
                    )
                },
            )
            Image(
                modifier = modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 6.dp, end = 6.dp),
                painter = painterResource(id = R.drawable.ic_editor_pencil),
                contentDescription = stringResource(id = R.string.edit_profile),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NickNameTextField(
    modifier: Modifier = Modifier,
    nickname: String,
    nicknameValidStatus: ProfileNicknameValidUiState,
    onNickNameChanged: (String) -> Unit,
    onKeyBoardTimerEnd: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(nickname) {
        delay(1.seconds)
        onKeyBoardTimerEnd()
    }

    Column(
        modifier = modifier
            .width(140.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicTextField(
            value = nickname,
            onValueChange = {
                onNickNameChanged(it)
            },
            modifier = modifier.fillMaxWidth(),
            enabled = true,
            textStyle = WithpeaceTheme.typography.body.copy(textAlign = TextAlign.Center),
            singleLine = true,
            maxLines = 1,
        ) {
            TextFieldDefaults.DecorationBox(
                value = nickname,
                innerTextField = it,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.enter_nickname),
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemGray2,
                    )
                },
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                ),
            )
        }
        Divider(
            color = if (nicknameValidStatus is ProfileNicknameValidUiState.Valid) WithpeaceTheme.colors.SystemBlack
            else WithpeaceTheme.colors.SystemError,
            modifier = modifier
                .width(140.dp)
                .height(1.dp),
        )
    }
    if (nicknameValidStatus !is ProfileNicknameValidUiState.Valid) {
        Text(
            text = if (nicknameValidStatus is ProfileNicknameValidUiState.InValidDuplicated) stringResource(
                R.string.nickname_duplicated,
            ) else stringResource(id = R.string.nickname_policy),
            style = WithpeaceTheme.typography.caption,
            color = WithpeaceTheme.colors.SystemError,
            modifier = modifier.padding(top = 4.dp),
        )
    }
}


@Composable
private fun EditCompletedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {
            onClick()
        },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .padding(
                bottom = 40.dp,
                end = WithpeaceTheme.padding.BasicHorizontalPadding,
                start = WithpeaceTheme.padding.BasicHorizontalPadding,
            )
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = WithpeaceTheme.colors.MainPink),
        shape = RoundedCornerShape(9.dp),
    ) {
        Text(
            style = WithpeaceTheme.typography.body,
            text = stringResource(R.string.edit_completed),
            modifier = Modifier.padding(vertical = 18.dp),
            color = WithpeaceTheme.colors.SystemWhite,
        )
    }
}

@Composable
fun ModifySaveDialog(
    modifier: Modifier = Modifier,
    onModifyDismissRequest: () -> Unit,
    onClickExit: () -> Unit,
    onClickSave: () -> Unit,
) {
    Dialog(onDismissRequest = { onModifyDismissRequest() }) {
        Surface(
            modifier = modifier
                .width(327.dp)
                .clip(RoundedCornerShape(10.dp)),
        ) {
            ModifySaveDialogContent(
                modifier = modifier,
                onClickExit = onClickExit,
                onClickSave = onClickSave,
            )
        }
    }
}

@Composable
fun ModifySaveDialogContent(
    modifier: Modifier,
    onClickExit: () -> Unit,
    onClickSave: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = modifier.height(24.dp))
        Text(
            text = stringResource(R.string.modify_save_request),
            style = WithpeaceTheme.typography.body,
            color = WithpeaceTheme.colors.SystemGray1,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = modifier.height(16.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextButton(
                modifier = modifier
                    .width(136.dp)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = WithpeaceTheme.colors.MainPink,
                    )
                    .background(WithpeaceTheme.colors.SystemWhite),
                onClick = { onClickExit() },
                content = {
                    Text(
                        text = stringResource(R.string.dialog_exit),
                        color = WithpeaceTheme.colors.MainPink,
                        style = WithpeaceTheme.typography.caption,
                    )
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                modifier = modifier
                    .width(136.dp)
                    .background(WithpeaceTheme.colors.MainPink, shape = RoundedCornerShape(10.dp)),
                onClick = { onClickSave() },
                content = {
                    Text(
                        text = stringResource(R.string.dialog_save),
                        color = WithpeaceTheme.colors.SystemWhite,
                        style = WithpeaceTheme.typography.caption,
                    )
                },
            )
        }
        Spacer(modifier = modifier.height(24.dp))
    }
}

@Composable
@Preview
fun ProfileEditorPreview() {
    WithpeaceTheme {
        ProfileEditorScreen(
            profileInfo = ProfileUiModel("nickname", ""),
            onClickBackButton = {},
            onNavigateToGallery = {},
            onEditCompleted = {},
            onNickNameChanged = {},
            onKeyBoardTimerEnd = {},
            nicknameValidStatus = ProfileNicknameValidUiState.Valid,
        )
    }
}
// uiModel 적용, glide 수정, response 값 전달
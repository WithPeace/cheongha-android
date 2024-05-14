package com.app.profileeditor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.profileeditor.uistate.ProfileEditUiEvent
import com.app.profileeditor.uistate.ProfileUiModel
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.ui.profile.NickNameEditor
import com.withpeace.withpeace.core.ui.profile.ProfileImageEditor
import com.withpeace.withpeace.core.ui.profile.ProfileNicknameValidUiState
import com.withpeace.withpeace.feature.profileeditor.R

@Composable
fun ProfileEditorRoute(
    onShowSnackBar: (message: String) -> Unit,
    onClickBackButton: () -> Unit,
    onNavigateToGallery: () -> Unit,
    viewModel: ProfileEditorViewModel,
    onUpdateSuccess: (nickname: String, imageUrl: String) -> Unit,
    onAuthExpired: () -> Unit,
) {
    var showAlertDialog by remember { mutableStateOf(false) }
    val profileInfo: ProfileUiModel by viewModel.profileEditUiState.collectAsStateWithLifecycle()
    BackHandler {
        if (profileInfo.isChanged) {
            showAlertDialog = true
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
                ProfileEditUiEvent.NicknameDuplicated -> onShowSnackBar("중복된 닉네임입니다.")

                ProfileEditUiEvent.NicknameInvalidFormat -> onShowSnackBar("닉네임 형식이 올바르지 않습니다.")

                ProfileEditUiEvent.UpdateFailure -> onShowSnackBar("서버와 통신 중 오류가 발생했습니다.")

                ProfileEditUiEvent.ProfileUnchanged -> onShowSnackBar("수정사항이 없습니다.")

                is ProfileEditUiEvent.UpdateSuccess -> {
                    onShowSnackBar("변경되었습니다.")
                    onUpdateSuccess(it.nickname, it.imageUrl)
                }

                ProfileEditUiEvent.UnAuthorized -> onAuthExpired()

            }
        }
    }


    ProfileEditorScreen(
        profileInfo = profileInfo,
        onClickBackButton = {
            if (profileInfo.isChanged) {
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
        isChanged = profileInfo.isChanged,
    )

}

@Composable
fun ProfileEditorScreen(
    profileInfo: ProfileUiModel,
    isChanged: Boolean,
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
            ProfileImageEditor(
                profileImage = profileInfo.profileImage,
                modifier = modifier,
                onNavigateToGallery = onNavigateToGallery,
                contentDescription = stringResource(id = R.string.edit_profile),
            )
            Spacer(modifier = modifier.height(24.dp))
            Text(
                text = stringResource(com.withpeace.withpeace.core.ui.R.string.nickname_policy),
                style = WithpeaceTheme.typography.caption,
                color = WithpeaceTheme.colors.SystemGray1,
            )
            Spacer(modifier = modifier.height(16.dp))
            NickNameEditor(
                nickname = profileInfo.nickname,
                onNickNameChanged = {
                    onNickNameChanged(it)
                },
                onKeyBoardTimerEnd = onKeyBoardTimerEnd,
                nicknameValidStatus = nicknameValidStatus,
                isChanged = isChanged,
            )

        }
        EditCompletedButton(onClick = { onEditCompleted() },isChanged = isChanged, nicknameValidUiState = nicknameValidStatus)
    }
}

@Composable
private fun EditCompletedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isChanged: Boolean,
    nicknameValidUiState: ProfileNicknameValidUiState,
) {
    val isClickable =
        isChanged and (nicknameValidUiState is ProfileNicknameValidUiState.Valid)
    Button(
        onClick = {
            if (isClickable) {
                onClick()
            }
        },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .padding(
                bottom = 40.dp,
                end = WithpeaceTheme.padding.BasicHorizontalPadding,
                start = WithpeaceTheme.padding.BasicHorizontalPadding,
            )
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = if (isClickable) WithpeaceTheme.colors.MainPurple else WithpeaceTheme.colors.SystemGray2),
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
                        color = WithpeaceTheme.colors.MainPurple,
                    )
                    .background(WithpeaceTheme.colors.SystemWhite),
                onClick = { onClickExit() },
                content = {
                    Text(
                        text = stringResource(R.string.dialog_exit),
                        color = WithpeaceTheme.colors.MainPurple,
                        style = WithpeaceTheme.typography.caption,
                    )
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                modifier = modifier
                    .width(136.dp)
                    .background(WithpeaceTheme.colors.MainPurple, shape = RoundedCornerShape(10.dp)),
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
            profileInfo = ProfileUiModel("nickname", "", false),
            onClickBackButton = {},
            onNavigateToGallery = {},
            onEditCompleted = {},
            onNickNameChanged = {},
            onKeyBoardTimerEnd = {},
            nicknameValidStatus = ProfileNicknameValidUiState.Valid,
            isChanged = false
        )
    }
}

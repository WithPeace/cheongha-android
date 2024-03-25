package com.app.profileeditor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import com.withpeace.withpeace.core.permission.ImagePermissionHelper
import com.withpeace.withpeace.feature.profileeditor.R

@Composable
fun ProfileEditorRoute(
    onShowSnackBar: (message: String) -> Unit,
    onClickBackButton: () -> Unit,
    onNavigateToGallery: () -> Unit,
    viewModel: ProfileEditorViewModel,
) {
    ProfileEditorScreen(
        profileInfo = viewModel.profileInfo.collectAsStateWithLifecycle().value,
        onClickBackButton = onClickBackButton,
        onNavigateToGallery = onNavigateToGallery,
        onEditCompleted = {},
        onNickNameChanged = viewModel::onNickNameChanged,
    )
}

@Composable
fun ProfileEditorScreen(
    profileInfo: ProfileInfo,
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onEditCompleted: () -> Unit,
    onNickNameChanged: (String) -> Unit,
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
                profileImage = profileInfo.profileImageUrl,
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
    val imageModifier = modifier.size(120.dp).clip(CircleShape)
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
                contentDescription = "프로필 수정",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NickNameTextField(
    modifier: Modifier = Modifier,
    nickname: String,
    onNickNameChanged: (String) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .width(140.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicTextField(
            value = nickname, onValueChange = onNickNameChanged,
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
                        text = "닉네임을 입력하세요",
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
            color = WithpeaceTheme.colors.SystemBlack,
            modifier = modifier
                .width(140.dp)
                .height(1.dp),
        )
        Text(text = "", modifier = modifier.padding(top = 4.dp))
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
        colors = ButtonDefaults.buttonColors(containerColor = WithpeaceTheme.colors.SystemGray2),
        shape = RoundedCornerShape(9.dp),
    ) {
        Text(
            style = WithpeaceTheme.typography.body,
            text = "변경 완료",
            modifier = Modifier.padding(vertical = 18.dp),
            color = WithpeaceTheme.colors.SystemWhite,
        )
    }
}

@Composable
@Preview
fun ProfileEditorPreview() {
    WithpeaceTheme {
        ProfileEditorScreen(
            profileInfo = ProfileInfo("nickname", null,""),
            onClickBackButton = {},
            onNavigateToGallery = {},
            onEditCompleted = {},
            onNickNameChanged = {},
        )
    }
}

package com.withpeace.withpeace.feature.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.profile.NickNameEditor
import com.withpeace.withpeace.core.ui.profile.ProfileImageEditor
import com.withpeace.withpeace.core.ui.profile.ProfileNicknameValidUiState
import com.withpeace.withpeace.feature.signup.uistate.SignUpUiEvent
import com.withpeace.withpeace.feature.signup.uistate.SignUpUiModel

@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel,
    onShowSnackBar: (message: String) -> Unit,
    onNavigateToGallery: () -> Unit,
    onSignUpSuccess: () -> Unit,
) {
    val signUpInfo = viewModel.signUpUiModel.collectAsStateWithLifecycle()
    val nicknameValidStatus = viewModel.profileNicknameValidUiState.collectAsStateWithLifecycle()
    SignUpScreen(
        isChanged = signUpInfo.value.nickname.isNotEmpty(),
        signUpInfo = signUpInfo.value,
        onNavigateToGallery = onNavigateToGallery,
        onSignUpClick = viewModel::signUp,
        onNickNameChanged = viewModel::onNickNameChanged,
        onKeyBoardTimerEnd = viewModel::verifyNickname,
        nicknameValidStatus = nicknameValidStatus.value,
    )
    LaunchedEffect(viewModel.signUpEvent) {
        viewModel.signUpEvent.collect {
            when (it) {
                SignUpUiEvent.EmptyNickname -> onShowSnackBar("닉네임 등록을 완료해주세요")
                SignUpUiEvent.NicknameDuplicated -> onShowSnackBar("중복된 닉네임입니다.")
                SignUpUiEvent.NicknameInvalidFormat -> onShowSnackBar("닉네임 형식이 올바르지 않습니다.")
                SignUpUiEvent.SignUpFail -> onShowSnackBar("서버와 통신 중 오류가 발생했습니다.")
                SignUpUiEvent.SignUpSuccess -> onSignUpSuccess()
                SignUpUiEvent.UnAuthorized -> onShowSnackBar("인가 되지 않은 게정이에요")
                SignUpUiEvent.VerifyFail -> onShowSnackBar("서버와 통신 중 오류가 발생했습니다.")
            }
        }
    }
}

@Composable
fun SignUpScreen(
    signUpInfo: SignUpUiModel,
    isChanged: Boolean,
    modifier: Modifier = Modifier,
    onNavigateToGallery: () -> Unit,
    onSignUpClick: () -> Unit,
    onNickNameChanged: (String) -> Unit,
    onKeyBoardTimerEnd: () -> Unit,
    nicknameValidStatus: ProfileNicknameValidUiState,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(104.dp))
            Text(
                text = stringResource(
                    R.string.create_profile,
                ),
                color = WithpeaceTheme.colors.SystemBlack,
                style = WithpeaceTheme.typography.title1,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(24.dp))
            ProfileImageEditor(
                profileImage = signUpInfo.profileImage,
                modifier = modifier,
                onNavigateToGallery = { onNavigateToGallery() },
                contentDescription = stringResource(R.string.set_up_profile_image),
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.nickname_policy),
                color = WithpeaceTheme.colors.SystemGray1,
                style = WithpeaceTheme.typography.caption,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            NickNameEditor(
                nickname = signUpInfo.nickname,
                onNickNameChanged = onNickNameChanged,
                isChanged = isChanged, // nickname.isNotEmpty()
                modifier = modifier,
                nicknameValidStatus = nicknameValidStatus,
                onKeyBoardTimerEnd = onKeyBoardTimerEnd,
            )
            Spacer(modifier = Modifier.height(72.dp))
        }
        SignUpButton(onClick = onSignUpClick)
    }
}

@Composable
private fun SignUpButton(
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
            text = stringResource(R.string.sign_up_completed),
            modifier = Modifier.padding(vertical = 18.dp),
            color = WithpeaceTheme.colors.SystemWhite,
        )
    }
}

@Preview(widthDp = 400, heightDp = 900, showBackground = true)
@Composable
fun SignUpPreview() {
    SignUpScreen(
        isChanged = false,
        onNavigateToGallery = {},
        onSignUpClick = {},
        onNickNameChanged = { },
        onKeyBoardTimerEnd = {},
        nicknameValidStatus = ProfileNicknameValidUiState.InValidDuplicated,
        signUpInfo = SignUpUiModel(nickname = "", profileImage = null),
    )
}
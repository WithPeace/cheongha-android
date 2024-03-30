package com.withpeace.withpeace.feature.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.TitleBar
import com.withpeace.withpeace.feature.mypage.uistate.MyPageUiEvent
import com.withpeace.withpeace.feature.mypage.uistate.MyPageUiState
import com.withpeace.withpeace.feature.mypage.uistate.ProfileInfoUiModel

@Composable
fun MyPageRoute(
    viewModel: MyPageViewModel = hiltViewModel(),
    onShowSnackBar: (message: String) -> Unit = {},
    onEditProfile: (nickname: String, profileImageUrl: String) -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
) {
    val mypageUiState by viewModel.myPageUiState.collectAsStateWithLifecycle()
    when (mypageUiState) {
        MyPageUiState.Loading -> {}
        is MyPageUiState.Success -> {}

    }
    LaunchedEffect(viewModel.myPageUiEvent) {
        viewModel.myPageUiEvent.collect {
            when(it) {
                MyPageUiEvent.UnAuthorizedError -> {
                    onShowSnackBar("인증에 실패했습니다")
                }
                MyPageUiEvent.GeneralError -> {
                    onShowSnackBar("서버와의 오류가 발생했습니다. 다시 시도해주세요.")
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getProfile()
    }
    val profileInfo = (mypageUiState as? MyPageUiState.Success)?.profileInfo ?: ProfileInfoUiModel(
        "nickname",
        "default.png",
        "",
    ) // TODO("없을 시 로컬에서 가지고 온다.")
    MyPageScreen(
        onEditProfile = {
            onEditProfile(it.nickname, it.profileImage)
        },
        onLogoutClick = onLogoutClick,
        onWithdrawClick = onWithdrawClick,
        profileInfo = profileInfo,
    )
}

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    onEditProfile: (ProfileInfoUiModel) -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    profileInfo: ProfileInfoUiModel,
) {
    Column(
        modifier,
    ) {
        TitleBar(title = stringResource(R.string.my_page))
        Column(modifier = modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding)) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val imageModifier = modifier
                        .size(54.dp)
                        .border(
                            BorderStroke(0.dp, Color.Transparent),
                            shape = CircleShape,
                        )
                    GlideImage(
                        modifier = imageModifier.clip(CircleShape),
                        imageModel = { profileInfo.profileImage },
                        failure = {
                            Image(
                                painterResource(id = R.drawable.ic_default_profile),
                                modifier = imageModifier,
                                contentDescription = "",
                            )
                        },
                    )
                    Text(
                        style = WithpeaceTheme.typography.body,
                        text = profileInfo.nickname,
                        modifier = modifier.padding(start = 8.dp),
                    )
                }
                TextButton(
                    onClick = { onEditProfile(profileInfo) },
                ) {
                    Text(
                        color = WithpeaceTheme.colors.MainPink,
                        text = stringResource(R.string.edit_profile),
                        style = WithpeaceTheme.typography.caption,
                    )
                }
            }
        }
        Spacer(modifier = modifier.height(16.dp))
        Divider(
            color = WithpeaceTheme.colors.SystemGray3,
            modifier = modifier
                .fillMaxWidth()
                .height(4.dp),
        )
        MyPageSections(
            modifier = modifier,
            onLogoutClick = onLogoutClick,
            onWithdrawClick = onWithdrawClick,
            email = profileInfo.email,
        )
    }
}

@Composable
fun MyPageSections(
    modifier: Modifier,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    email: String,
) {
    Column(modifier = modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding)) {
        AccountSection(modifier, email = email)
        Divider(
            modifier = modifier
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        EtcSection(modifier, onLogoutClick = onLogoutClick, onWithdrawClick = onWithdrawClick)
    }
}

@Composable
private fun AccountSection(modifier: Modifier, email: String) {
    Section(title = stringResource(R.string.account)) {
        Spacer(modifier = modifier.height(16.dp))
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(R.string.connected_account),
                style = WithpeaceTheme.typography.body,
                color = WithpeaceTheme.colors.SystemBlack,
            )
            Text(
                text = email,
                style = WithpeaceTheme.typography.caption,
                color = WithpeaceTheme.colors.SystemGray2,
            )
        }
    }
}

@Composable
private fun EtcSection(
    modifier: Modifier,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
) {
    Section(title = stringResource(R.string.etc)) {
        Spacer(modifier = modifier.height(8.dp))
        Text(
            text = stringResource(R.string.logout),
            style = WithpeaceTheme.typography.body,
            color = WithpeaceTheme.colors.SystemBlack,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onLogoutClick()
                }
                .padding(vertical = 8.dp),
        )
        Text(
            text = stringResource(R.string.withdraw),
            style = WithpeaceTheme.typography.body,
            color = WithpeaceTheme.colors.SystemBlack,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onWithdrawClick()
                }
                .padding(vertical = 8.dp),
        )
    }
}

@Composable
private fun Section(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    Column {
        Spacer(modifier = modifier.height(16.dp))
        Text(text = title, style = WithpeaceTheme.typography.caption, color = Color(0xFF858585))
        content()
        Spacer(modifier = modifier.height(16.dp))
    }

}

@Preview
@Composable
fun MyPagePreview() {
    WithpeaceTheme {
        MyPageScreen(
            onEditProfile = {
            },
            modifier = Modifier,
            onLogoutClick = {},
            onWithdrawClick = {},
            profileInfo = ProfileInfoUiModel("닉네임닉네임", "", "abc@gmail.com"),
        )
    }
}
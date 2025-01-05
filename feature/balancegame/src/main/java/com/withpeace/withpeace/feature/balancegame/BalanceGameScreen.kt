package com.withpeace.withpeace.feature.balancegame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar

@Composable
fun BalanceGameRoute(
    viewModel: BalanceGameViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    BalanceGameScreen(
        onClickBackButton = onClickBackButton,
        onClickBeforeDay = viewModel::onClickBeforeDay,
        onClickAfterDay = viewModel::onClickAfterDay,
    )
}

@Composable
fun BalanceGameScreen(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
    onClickBeforeDay: () -> Unit,
    onClickAfterDay: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
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
                        text = "밸런스 게임",
                        style = WithpeaceTheme.typography.title1,
                        color = WithpeaceTheme.colors.SystemBlack,
                    )
                },
            )
            HorizontalDivider(
                color = WithpeaceTheme.colors.SystemGray3,
                modifier = modifier.fillMaxWidth(),
                thickness = 1.dp,
            )
            Spacer(modifier = modifier.height(24.dp))
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Image(
                    modifier = modifier
                        .align(Alignment.CenterStart)
                        .clickable { onClickBeforeDay() },
                    painter = painterResource(R.drawable.ic_left),
                    contentDescription = "전날",
                )
                Text(
                    text = "오늘의 밸런스 게임",
                    modifier = modifier
                        .align(Alignment.Center)
                        .background(
                            color = WithpeaceTheme.colors.SubPurple,
                            shape = RoundedCornerShape(999.dp),
                        )
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    style = WithpeaceTheme.typography.disablePolicyTitle,
                    color = WithpeaceTheme.colors.MainPurple,
                )
                Image(
                    modifier = modifier.align(Alignment.CenterEnd),
                    painter = painterResource(R.drawable.ic_right),
                    contentDescription = "다음날",
                )
            }
            Spacer(modifier = modifier.height(24.dp))
            Text(
                "내가 가지고 싶은 복지 카드는?",
                style = WithpeaceTheme.typography.title2,
                color = WithpeaceTheme.colors.SnackbarBlack,
            )
            Spacer(modifier = modifier.height(32.dp))
            // isActive && selectedStatus == null & isToday
            //
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Column(
                    modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = WithpeaceTheme.colors.MainPurple,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .align(Alignment.BottomCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = modifier.height(28.dp))
                    Text(
                        "오프라인에서만 쓸 수 있는 80만원 카드",
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SnackbarBlack,
                    )
                    Spacer(modifier = modifier.height(20.dp))
                    Row(
                        modifier.padding(start = 28.dp, end = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        LinearProgressIndicator(
                            gapSize = (-10).dp, // 갭을 -2이하로 안할시 진행도와 남은 track이 분리되어 보임
                            strokeCap = StrokeCap.Round,
                            trackColor = Color(0xffd9d9d9),
                            drawStopIndicator = {},
                            color = WithpeaceTheme.colors.MainPurple,
                            progress = { 0.7f },
                        )
                        Spacer(modifier.width(6.dp))
                        Text(
                            "68%",
                            style = WithpeaceTheme.typography.Tag,
                            color = WithpeaceTheme.colors.SystemGray1,
                        )
                    }
                    Spacer(modifier = modifier.height(20.dp))
                }
                Image(
                    painter = painterResource(R.drawable.ic_a),
                    modifier = modifier
                        .offset((-7).dp, (-5).dp)
                        .align(Alignment.TopStart)
                        .background(
                            shape = CircleShape,
                            color = WithpeaceTheme.colors.MainPurple,
                        )
                        .padding(horizontal = 8.3.dp, vertical = 7.7.dp),
                    contentDescription = "a 선택",
                )
            }
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = "VS",
                style = WithpeaceTheme.typography.title2,
                color = WithpeaceTheme.colors.MainPurple,
            )
            Spacer(modifier = modifier.height(16.dp))
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Column(
                    modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = WithpeaceTheme.colors.MainPurple,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .align(Alignment.BottomCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = modifier.height(28.dp))
                    Text(
                        "오프라인에서만 쓸 수 있는 80만원 카드",
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SnackbarBlack,
                    )
                    Spacer(modifier = modifier.height(20.dp))
                    Row(
                        modifier.padding(start = 28.dp, end = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        LinearProgressIndicator(
                            gapSize = (-10).dp, // 갭을 -2이하로 안할시 진행도와 남은 track이 분리되어 보임
                            strokeCap = StrokeCap.Round,
                            trackColor = Color(0xffd9d9d9),
                            drawStopIndicator = {},
                            color = WithpeaceTheme.colors.MainPurple,
                            progress = { 0.7f },
                        )
                        Spacer(modifier.width(6.dp))
                        Text(
                            "68%",
                            style = WithpeaceTheme.typography.Tag,
                            color = WithpeaceTheme.colors.SystemGray1,
                        )
                    }
                    Spacer(modifier = modifier.height(20.dp))
                }
                Image(
                    painter = painterResource(R.drawable.ic_b),
                    modifier = modifier
                        .offset((-7).dp, (-5).dp)
                        .align(Alignment.TopStart)
                        .background(
                            shape = CircleShape,
                            color = WithpeaceTheme.colors.MainPurple,
                        )
                        .padding(horizontal = 9.0.dp, vertical = 7.7.dp),
                    contentDescription = "b 선택",
                )
            }
            Spacer(modifier = modifier.height(12.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = "",
                )
                Spacer(modifier = modifier.width(2.dp))
                Text(
                    "100명 참여",
                    style = WithpeaceTheme.typography.medium16LineHeight20,
                    color = WithpeaceTheme.colors.SystemGray2,
                )
            }
        }
    }
}

@Composable
@Preview
fun BalanceGamePreview() {
    WithpeaceTheme {
        BalanceGameScreen(
            onClickBackButton = {},
            onClickBeforeDay = {},
        ) { }
    }
}
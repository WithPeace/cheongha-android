package com.withpeace.withpeace.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.analytics.TrackScreenViewEvent

@Composable
fun HomeRoute(
    onShowSnackBar: (message: String) -> Unit = {},
    onNavigationSnackBar: (message: String) -> Unit  = {},
    viewModel: HomeViewModel = hiltViewModel(),
    onPolicyClick: (String) -> Unit,
) {
    HomeScreen()
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        HomeHeader(
            modifier = modifier,
        )
        HorizontalDivider(
            modifier = modifier.height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )

        ScrollSection(modifier)

    }
    TrackScreenViewEvent(screenName = "home")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ScrollSection(modifier: Modifier) {
    val builder = rememberBalloonBuilder {
        setIsVisibleArrow(false)
        setWidth(BalloonSizeSpec.WRAP)
        setHeight(BalloonSizeSpec.WRAP)
        setPadding(12)
        setCornerRadius(12f)
        setBackgroundColor(Color(0xFFF9FBFB))
        setBalloonAnimation(BalloonAnimation.FADE)
        setArrowSize(0)
    }


    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = WithpeaceTheme.colors.Gray3_70)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
            ) {
                Row{
                    Text(
                        text = "관심 키워드",
                        style = WithpeaceTheme.typography.caption,
                        color = WithpeaceTheme.colors.SystemGray1,
                    )
                    Spacer(modifier = modifier.width(4.dp))
                    Balloon(
                        builder = builder,
                        balloonContent = {
                            Text(
                                text = "선택하신 관심 정책 분야 및 지역으로,\n" +
                                    "핫한 정책 및 맞춤정책 추천 시 해당 키워드 기반으\n로 추천이 진행됩니다.",
                                color = Color(0x9C101014),
                                style = WithpeaceTheme.typography.homePolicyContent,
                            )
                        },
                    ) { balloonWindow ->
                        Image(
                            modifier = modifier.clickable {
                                balloonWindow.showAsDropDown()
                            },
                            painter = painterResource(id = R.drawable.ic_circle_info),
                            contentDescription = "",
                        )
                    }
                }

                Spacer(modifier = modifier.height(8.dp))
                FlowRow {
                    Image(
                        painter = painterResource(id = R.drawable.ic_filter),
                        modifier = modifier
                            .background(
                                color = WithpeaceTheme.colors.SubPurple,
                                shape = CircleShape,
                            )
                            .padding(4.dp)
                            .size(16.dp),
                        contentDescription = "",
                    )
                    List(5) { //TODO("데이터 변경")
                        Spacer(modifier = modifier.width(8.dp))
                        Text(
                            text = "#부산",
                            style = WithpeaceTheme.typography.Tag,
                            color = WithpeaceTheme.colors.MainPurple,
                            modifier = modifier
                                .background(
                                    color = WithpeaceTheme.colors.SubPurple,
                                    shape = RoundedCornerShape(7.dp),
                                )
                                .padding(6.dp),
                        )
                    }
                }
            }
            Spacer(modifier = modifier.height(16.dp))
            Text(
                modifier = modifier.padding(horizontal = 24.dp),
                text = "지금 핫한 정책",
                color = WithpeaceTheme.colors.SnackbarBlack,
                style = WithpeaceTheme.typography.title2,
            )
            Spacer(modifier = modifier.height(16.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(6) {
                    Column {
                        Image(
                            modifier = modifier
                                .size(140.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            painter = painterResource(id = com.withpeace.withpeace.core.ui.R.drawable.ic_policy_participation_right),
                            contentDescription = "",
                        )
                        Spacer(modifier = modifier.height(8.dp))
                        Text(
                            modifier = modifier.width(140.dp),
                            text = "울산대학교 대학일자리플러스센터(거점형)",
                            style = WithpeaceTheme.typography.caption,
                            color = WithpeaceTheme.colors.SnackbarBlack,
                        )
                    }
                }
            }
            Spacer(modifier = modifier.height(24.dp))
            Text(
                modifier = modifier.padding(horizontal = 24.dp),
                text = "맞춤 정책을 추천해드릴게요!",
                color = WithpeaceTheme.colors.SnackbarBlack,
                style = WithpeaceTheme.typography.title2,
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                modifier = modifier.padding(horizontal = 24.dp),
                text = "관심 키워드와 추천 알고리즘을 기반으로 정책을 추천해드려요.",
                style = WithpeaceTheme.typography.homePolicyContent,
                color = WithpeaceTheme.colors.SystemGray1,
            )
            Spacer(modifier = modifier.height(16.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(6) {
                    Column {
                        Image(
                            modifier = modifier
                                .size(140.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            painter = painterResource(id = com.withpeace.withpeace.core.ui.R.drawable.ic_policy_participation_right),
                            contentDescription = "",
                        )
                        Spacer(modifier = modifier.height(8.dp))
                        Text(
                            modifier = modifier.width(140.dp),
                            text = "울산대학교 대학일자리플러스센터(거점형)",
                            style = WithpeaceTheme.typography.caption,
                            color = WithpeaceTheme.colors.SnackbarBlack,
                        )
                    }
                }
            }
            Spacer(modifier = modifier.height(24.dp))
            Text(
                modifier = modifier.padding(horizontal = 24.dp),
                text = "커뮤니티",
                color = WithpeaceTheme.colors.SnackbarBlack,
                style = WithpeaceTheme.typography.title2,
            )
            Spacer(modifier = modifier.height(16.dp))
        }
        items(6) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(start = 24.dp, end = 17.dp),
            ) {
                Text(text = "자유 게시판")
                Spacer(modifier = modifier.width(16.dp))
                Text(
                    style = WithpeaceTheme.typography.caption,
                    text = "토트넘 vs K리그 누가 이길 것 같나요? 토트넘 vs K리그 누가 이길 것 같나요?",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp),
    ) {
        Image(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .size(47.dp),
            painter = painterResource(id = R.drawable.ic_home_logo),
            contentDescription = stringResource(R.string.cheongha_logo),
        )
        // Image(
        //     modifier = modifier
        //         .size(24.dp)
        //         .align(Alignment.CenterEnd)
        //         .clickable {
        //             showBottomSheet = true
        //         },
        //     painter = painterResource(id = R.drawable.ic_filter),
        //     contentDescription = stringResource(R.string.filter),
        // )
    }
}
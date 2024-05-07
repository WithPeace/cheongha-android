package com.withpeace.withpeace.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.util.dropShadow

@Composable
fun HomeRoute(
    onShowSnackBar: (message: String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    HomeScreen()
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        HomeHeader(modifier)
        HorizontalDivider(
            modifier = modifier.height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FB))
                .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = modifier.height(8.dp))
            LazyColumn(modifier = modifier.fillMaxSize()) {
                item {
                    Spacer(modifier = modifier.height(8.dp))
                    YouthPolicyCard(
                        modifier = modifier,
                        youthPolicy = YouthPolicyUiModel(
                            id = 0,
                            title = "청년창업 지원사업 예비 창업자를 위한 정책입니다.",
                            content = "생애 최초로 창업에 도전하는 만 29세 이하 청년 예비 창업자들의 성공을 위해 지원하는 정책입니다. 이 정책을 하면은 정말 좋습니다",
                            region = "서울",
                            ageRange = "만29세 이하",
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 24.dp),
    ) {
        //TODO("로고로 변경")
        Image(
            modifier = modifier
                .size(36.dp)
                .clip(CircleShape)
                .align(Alignment.Center),
            painter = painterResource(id = com.withpeace.withpeace.core.ui.R.drawable.ic_default_profile),
            contentDescription = stringResource(R.string.cheongha_logo),
        )
        Image(
            modifier = modifier
                .size(24.dp)
                .align(Alignment.CenterEnd),
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = stringResource(R.string.filter),
        )
    }
}

@Composable
private fun YouthPolicyCard(
    modifier: Modifier,
    youthPolicy: YouthPolicyUiModel,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = WithpeaceTheme.colors.SystemWhite,
                shape = RoundedCornerShape(size = 10.dp),
            )
            .dropShadow(
                color = Color(0x1A000000),
                blurRadius = 4.dp,
                spreadRadius = 2.dp,
                borderRadius = 10.dp
            ),
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .background(WithpeaceTheme.colors.SystemWhite)
                .padding(16.dp),
        ) {
            val (
                title, content,
                region, ageRange, thumbnail,
            ) = createRefs()

            Text(
                text = youthPolicy.title,
                modifier.constrainAs(
                    title,
                    constrainBlock = {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(thumbnail.start, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    },
                ),
                style = WithpeaceTheme.typography.title2, //TODO("style 디자인 가이드라인에 맞게 요청")
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                text = youthPolicy.content,
                modifier = modifier
                    .constrainAs(
                        content,
                        constrainBlock = {
                            top.linkTo(title.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(thumbnail.start, margin = 8.dp)
                            width = Dimension.fillToConstraints
                        },
                    ),
                style = WithpeaceTheme.typography.caption, //TODO("style 디자인 가이드라인에 맞게 요청")
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )

            Text(
                text = youthPolicy.region,
                color = WithpeaceTheme.colors.MainPink,
                modifier = modifier
                    .constrainAs(
                        region,
                        constrainBlock = {
                            top.linkTo(content.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                    )
                    .background(
                        color = WithpeaceTheme.colors.SubPink,
                        shape = RoundedCornerShape(size = 5.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                //TODO("Text style 디자인 가이드라인에 맞게 요청")
            )

            Text(
                text = youthPolicy.ageRange,
                color = WithpeaceTheme.colors.SystemGray1,
                modifier = modifier
                    .constrainAs(
                        ageRange,
                        constrainBlock = {
                            top.linkTo(region.top)
                            start.linkTo(region.end, margin = 8.dp)
                            bottom.linkTo(parent.bottom)
                        },
                    )
                    .background(
                        color = WithpeaceTheme.colors.SystemGray3,
                        shape = RoundedCornerShape(size = 5.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                //TODO("Text style 디자인 가이드라인에 맞게 요청")
            )


            Image(
                modifier = modifier
                    .size(57.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .constrainAs(
                        ref = thumbnail,
                        constrainBlock = {
                            start.linkTo(title.end)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        },
                    ),
                painter = painterResource(id = R.drawable.ic_home_thumbnail_example),
                contentDescription = "임시",
            ) //TODO("이미지 변경")
        }
    }
}

@Composable
@Preview
fun HomePreview() {
    WithpeaceTheme {
        HomeScreen()
    }
}

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.ui.DateUiModel
import com.withpeace.withpeace.core.ui.comment.CommentSection
import com.withpeace.withpeace.core.ui.comment.CommentSize
import com.withpeace.withpeace.core.ui.comment.RegisterCommentSection
import com.withpeace.withpeace.core.ui.post.CommentUiModel
import com.withpeace.withpeace.core.ui.post.CommentUserUiModel
import java.time.LocalDateTime

@Composable
fun BalanceGameRoute(
    viewModel: BalanceGameViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
) {

    val uiState = viewModel.balanceGamesState.collectAsStateWithLifecycle()
    val currentPage = viewModel.currentPage.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(
        pageCount = { (uiState.value as? BalanceGameUIState.Success)?.games?.size ?: 0 },
    )

    LaunchedEffect(uiState.value) {
        // rememberPagerState
        if (uiState.value is BalanceGameUIState.Success) {
            //TODO 로딩후에는 애니메이션은 안됨
            pagerState.animateScrollToPage(currentPage.value)
        }
    }
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect {
            when (it) {
                //TODO 1. 밸런스 게임 선택기능
                // 클릭했을 때
                // 2. 댓글 기능
                BalanceGameUiEvent.NextPage -> {
                    if (uiState.value is BalanceGameUIState.Success) {
                        pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                    }
                }

                BalanceGameUiEvent.PreviousPage -> {
                    if (uiState.value is BalanceGameUIState.Success) {
                        pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                    }
                }
            }
        }
    }
    BalanceGameScreen(
        onClickBackButton = onClickBackButton,
        onClickBeforeDay = viewModel::onClickBeforeDay,
        onClickAfterDay = viewModel::onClickAfterDay,
        uiState = uiState.value,
        pagerState = pagerState,
    )
}

@Composable
fun BalanceGameScreen(
    modifier: Modifier = Modifier,
    uiState: BalanceGameUIState,
    pagerState: PagerState,
    onClickBackButton: () -> Unit,
    onClickBeforeDay: () -> Unit,
    onClickAfterDay: () -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
        bottomBar = {
            Column(modifier = modifier.background(color = WithpeaceTheme.colors.SystemWhite)) {
                RegisterCommentSection()
            }

        },
    ) { innerPadding ->
        if (uiState is BalanceGameUIState.Success) {
            val balanceGame = uiState.games
            Column(
                modifier = modifier
                    .padding(innerPadding)
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

                }
                HorizontalPager(
                    userScrollEnabled = false,
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    state = pagerState,
                ) {
                    LazyColumn {
                        item {
                            Column(
                                modifier = modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = modifier.height(24.dp))
                                Box(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp),
                                ) {
                                    Image(
                                        modifier = modifier
                                            .align(Alignment.CenterStart)
                                            .clickable {
                                                if (balanceGame[it].hasPrevious) {
                                                    onClickBeforeDay()
                                                }
                                            },
                                        painter = if (balanceGame[it].hasPrevious) painterResource(R.drawable.ic_left) else painterResource(
                                            R.drawable.ic_left_disable,
                                        ),
                                        contentDescription = "전날",
                                    )
                                    Text(
                                        text = balanceGame[it].date,
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
                                        modifier = modifier
                                            .align(Alignment.CenterEnd)
                                            .clickable {
                                                if (balanceGame[it].hasNext) {
                                                    onClickAfterDay()
                                                }
                                            },
                                        painter = if (balanceGame[it].hasNext) painterResource(R.drawable.ic_right) else painterResource(
                                            R.drawable.ic_right_disable,
                                        ),
                                        contentDescription = "다음날",
                                    )
                                }
                                Spacer(modifier = modifier.height(24.dp))
                                Text(
                                    balanceGame[it].gameTitle,
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
                                            balanceGame[it].optionA,
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
                                                progress = { balanceGame[it].percentageOptionA.toFloat() / 100 },
                                            )
                                            Spacer(modifier.width(6.dp))
                                            Text(
                                                balanceGame[it].percentageOptionA.toString() + "%",
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
                                            balanceGame[it].optionB,
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
                                                progress = { balanceGame[it].percentageOptionB.toFloat() / 100 },
                                            )
                                            Spacer(modifier.width(6.dp))
                                            Text(
                                                balanceGame[it].percentageOptionB.toString() + "%",
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
                                    verticalAlignment = Alignment.CenterVertically,
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
                                        "${balanceGame[it].totalCount}명 참여",
                                        style = WithpeaceTheme.typography.medium16LineHeight20,
                                        color = WithpeaceTheme.colors.SystemGray2,
                                    )
                                }
                                Spacer(modifier = modifier.height(8.dp))
                                CommentSize(modifier.fillMaxWidth(), commentSize = 5)
                                Spacer(modifier = modifier.height(8.dp))
                                HorizontalDivider(
                                    thickness = 4.dp,
                                    color = WithpeaceTheme.colors.SystemGray3,
                                )
                            }
                        }
                        CommentSection(
                            comments = List(5) {
                                CommentUiModel(
                                    id = it.toLong(),
                                    content = "가나다",
                                    createDate = DateUiModel(LocalDateTime.now()),
                                    commentUser = CommentUserUiModel(
                                        id = it.toLong(),
                                        nickname = "청하다",
                                        profileImageUrl = "",
                                    ),
                                    isMyComment = false,
                                )
                            },
                            onReportComment = { _, _ -> },
                        )
                    }
                }
            }
        } else if (uiState is BalanceGameUIState.Loading
        ) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .background(WithpeaceTheme.colors.SystemWhite),
                    color = WithpeaceTheme.colors.MainPurple,
                )
            }
        } else {
            Box(Modifier.fillMaxSize()) {
                Text(
                    text = "네트워크 상태를 확인해주세요.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .background(WithpeaceTheme.colors.SystemWhite),
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
            pagerState = PagerState { 0 },
            onClickBackButton = {},
            onClickBeforeDay = {},
            uiState = BalanceGameUIState.Success(listOf()),
        ) { }

    }
}
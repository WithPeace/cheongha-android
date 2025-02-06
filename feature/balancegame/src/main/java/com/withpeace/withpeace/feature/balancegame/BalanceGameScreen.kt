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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.withpeace.withpeace.core.ui.comment.CommentSize
import com.withpeace.withpeace.core.ui.comment.RegisterCommentSection
import com.withpeace.withpeace.core.ui.post.ReportTypeUiModel
import com.withpeace.withpeace.feature.balancegame.comment.CommentSection

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
    val comment = viewModel.commentText.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

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
                BalanceGameUiEvent.RegisterCommentFailure -> {
                    onShowSnackBar("댓글 등록 실패하였습니다")
                }

                BalanceGameUiEvent.RegisterCommentSuccess -> {
                    lazyListState.fullAnimatedScroll()
                }

                BalanceGameUiEvent.ReportCommentFailure -> onShowSnackBar("신고에 실패하였습니다")
                BalanceGameUiEvent.ReportCommentSuccess -> onShowSnackBar("신고 되었습니다")
                BalanceGameUiEvent.UnAuthorized -> {}
                BalanceGameUiEvent.ReportCommentDuplicated -> {
                    onShowSnackBar("이미 신고한 댓글입니다.")
                }
            }
        }
    }
    BalanceGameScreen(
        lazyListState = lazyListState,
        onClickBackButton = onClickBackButton,
        onClickBeforeDay = viewModel::onClickBeforeDay,
        onClickAfterDay = viewModel::onClickAfterDay,
        uiState = uiState.value,
        pagerState = pagerState,
        onSelectA = viewModel::onSelectA,
        onSelectB = viewModel::onSelectB,
        onClickRegisterButton = viewModel::onClickCommentRegister,
        onTextChanged = viewModel::onCommentTextChanged,
        onReportComment = viewModel::reportComment,
        comment = comment.value,
    )
}

@Composable
fun BalanceGameScreen(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    uiState: BalanceGameUIState,
    pagerState: PagerState,
    onClickBackButton: () -> Unit,
    onClickBeforeDay: () -> Unit,
    onClickAfterDay: () -> Unit,
    onSelectA: (BalanceGameUiModel) -> Unit,
    onSelectB: (BalanceGameUiModel) -> Unit,
    onClickRegisterButton: () -> Unit,
    onTextChanged: (String) -> Unit,
    onReportComment: (Long, ReportTypeUiModel) -> Unit,
    comment: String,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
        bottomBar = {
            Column(modifier = modifier.background(color = WithpeaceTheme.colors.SystemWhite)) {
                RegisterCommentSection(
                    onClickRegisterButton = onClickRegisterButton,
                    onTextChanged = onTextChanged,
                    text = comment,
                )
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
                    state = pagerState,
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                    ) {
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
                                GameA(modifier, onSelectA, balanceGame[it])
                                Spacer(modifier = modifier.height(16.dp))
                                Text(
                                    text = "VS",
                                    style = WithpeaceTheme.typography.title2,
                                    color = WithpeaceTheme.colors.MainPurple,
                                )
                                Spacer(modifier = modifier.height(16.dp))
                                GameB(
                                    modifier = modifier,
                                    onSelectB = onSelectB,
                                    balanceGame = balanceGame[it],
                                )
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
                                CommentSize(
                                    modifier.fillMaxWidth(),
                                    commentSize = balanceGame[it].commentCount,
                                )
                                Spacer(modifier = modifier.height(8.dp))
                                HorizontalDivider(
                                    thickness = 4.dp,
                                    color = WithpeaceTheme.colors.SystemGray3,
                                )
                            }
                        }
                        CommentSection(
                            comments = balanceGame[it].comments,
                            onReportComment = { id, reportType ->
                                onReportComment(id, reportType)
                            },
                        ) //TODO 댓글 달기, 신고하기 API 연동
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
private fun GameA(
    modifier: Modifier,
    onSelectA: (BalanceGameUiModel) -> Unit,
    balanceGame: BalanceGameUiModel,
) {
    if (balanceGame.date.contains("오늘의") && balanceGame.userChoice == null) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color(color = 0xFFF9FBFB))
                .padding(horizontal = 24.dp)
                .clickable {
                    onSelectA(balanceGame)
                },
        ) {
            Column(
                modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = modifier.height(28.dp))
                Text(
                    balanceGame.optionA,
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
                        progress = { balanceGame.percentageOptionA.toFloat() / 100 },
                    )
                    Spacer(modifier.width(6.dp))
                    Text(
                        balanceGame.percentageOptionA.toString() + "%",
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
                        color = WithpeaceTheme.colors.SystemGray2,
                    )
                    .padding(horizontal = 8.3.dp, vertical = 7.7.dp),
                contentDescription = "a 선택",
            )
        }
    } else {
        Box(
            modifier = if (balanceGame.userChoice == "OPTION_A" ||
                (balanceGame.userChoice == null
                    && balanceGame.percentageOptionA > balanceGame.percentageOptionB
                    && !balanceGame.date.contains(
                    "오늘의",
                ))
            ) modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(
                    WithpeaceTheme.colors.SystemWhite,
                )
                .clickable {
                    onSelectA(balanceGame)
                } else modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(
                    shape = RoundedCornerShape(16.dp),
                    color = WithpeaceTheme.colors.SystemGray3,
                )
                .clickable {
                    onSelectA(balanceGame)
                },
        ) {
            Column(
                modifier = if (balanceGame.userChoice == "OPTION_A" ||
                    (balanceGame.userChoice == null
                        && balanceGame.percentageOptionA > balanceGame.percentageOptionB
                        && !balanceGame.date.contains(
                        "오늘의",
                    ))
                ) modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = WithpeaceTheme.colors.MainPurple,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .align(Alignment.BottomCenter)
                else modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = modifier.height(28.dp))
                Text(
                    balanceGame.optionA,
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
                        color = if (balanceGame.userChoice == "OPTION_A" ||
                            (balanceGame.userChoice == null
                                && balanceGame.percentageOptionA > balanceGame.percentageOptionB
                                && !balanceGame.date.contains(
                                "오늘의",
                            ))
                        ) WithpeaceTheme.colors.MainPurple else WithpeaceTheme.colors.SystemGray2,
                        progress = { balanceGame.percentageOptionA.toFloat() / 100 },
                    )
                    Spacer(modifier.width(6.dp))
                    Text(
                        balanceGame.percentageOptionA.toString() + "%",
                        style = WithpeaceTheme.typography.Tag,
                        color = WithpeaceTheme.colors.SystemGray1,
                    )
                }
                Spacer(modifier = modifier.height(20.dp))
            }
            if (balanceGame.userChoice == "OPTION_A") {
                Image(
                    painter =
                    painterResource(R.drawable.ic_balance_game_selection),
                    modifier = modifier
                        .offset((-7).dp, (-5).dp)
                        .align(Alignment.TopStart),
                    contentDescription = "a 선택",
                )
            } else {
                Image(
                    painter =
                    painterResource(R.drawable.ic_a),
                    modifier = modifier
                        .offset((-7).dp, (-5).dp)
                        .align(Alignment.TopStart)
                        .background(
                            shape = CircleShape,
                            color = if (balanceGame.percentageOptionA > balanceGame.percentageOptionB) WithpeaceTheme.colors.MainPurple else WithpeaceTheme.colors.SystemGray2, // 사람들이 a를 더 많이 선택했으면, 안했으면
                        )
                        .padding(horizontal = 8.3.dp, vertical = 7.7.dp),
                    contentDescription = "a 선택",
                )
            }

        }
    }
}

@Composable
private fun GameB(
    modifier: Modifier,
    onSelectB: (BalanceGameUiModel) -> Unit,
    balanceGame: BalanceGameUiModel,
) {
    if (balanceGame.date.contains("오늘의") && balanceGame.userChoice == null) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color(color = 0xFFF9FBFB))
                .padding(horizontal = 24.dp)
                .clickable {
                    onSelectB(balanceGame)
                },
        ) {
            Column(
                modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = modifier.height(28.dp))
                Text(
                    balanceGame.optionB,
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
                        progress = { balanceGame.percentageOptionB.toFloat() / 100 },
                    )
                    Spacer(modifier.width(6.dp))
                    Text(
                        balanceGame.percentageOptionB.toString() + "%",
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
                        color = WithpeaceTheme.colors.SystemGray2,
                    )
                    .padding(horizontal = 9.0.dp, vertical = 7.7.dp),
                contentDescription = "b 선택",
            )
        }
    } else {
        Box(
            modifier = if (balanceGame.userChoice == "OPTION_B" ||
                (balanceGame.userChoice == null
                    && balanceGame.percentageOptionA < balanceGame.percentageOptionB
                    && !balanceGame.date.contains(
                    "오늘의",
                ))
            ) modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(
                    WithpeaceTheme.colors.SystemWhite,
                )
                .clickable {
                    onSelectB(balanceGame)
                } else modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(
                    shape = RoundedCornerShape(16.dp),
                    color = WithpeaceTheme.colors.SystemGray3,
                )
                .clickable {
                    onSelectB(balanceGame)
                },
        ) {
            Column(
                modifier = if (balanceGame.userChoice == "OPTION_B" ||
                    (balanceGame.userChoice == null
                        && balanceGame.percentageOptionA < balanceGame.percentageOptionB
                        && !balanceGame.date.contains(
                        "오늘의",
                    ))
                ) modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = WithpeaceTheme.colors.MainPurple,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .align(Alignment.BottomCenter)
                else modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = modifier.height(28.dp))
                Text(
                    balanceGame.optionB,
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
                        color = if (balanceGame.userChoice == "OPTION_B" ||
                            (balanceGame.userChoice == null
                                && balanceGame.percentageOptionA < balanceGame.percentageOptionB
                                && !balanceGame.date.contains(
                                "오늘의",
                            ))
                        ) WithpeaceTheme.colors.MainPurple else WithpeaceTheme.colors.SystemGray2,
                        progress = { balanceGame.percentageOptionB.toFloat() / 100 },
                    )
                    Spacer(modifier.width(6.dp))
                    Text(
                        balanceGame.percentageOptionB.toString() + "%",
                        style = WithpeaceTheme.typography.Tag,
                        color = WithpeaceTheme.colors.SystemGray1,
                    )
                }
                Spacer(modifier = modifier.height(20.dp))
            }
            if (balanceGame.userChoice == "OPTION_B") {
                Image(
                    painter =
                    painterResource(R.drawable.ic_balance_game_selection),
                    modifier = modifier
                        .offset((-7).dp, (-5).dp)
                        .align(Alignment.TopStart),
                    contentDescription = "b 선택",
                )
            } else {
                Image(
                    painter =
                    painterResource(R.drawable.ic_b),
                    modifier = modifier
                        .offset((-7).dp, (-5).dp)
                        .align(Alignment.TopStart)
                        .background(
                            shape = CircleShape,
                            color = if (balanceGame.percentageOptionA < balanceGame.percentageOptionB) WithpeaceTheme.colors.MainPurple else WithpeaceTheme.colors.SystemGray2, // 사람들이 a를 더 많이 선택했으면, 안했으면
                        )
                        .padding(horizontal = 8.3.dp, vertical = 7.7.dp),
                    contentDescription = "a 선택",
                )
            }
        }
    }
}

private suspend fun LazyListState.fullAnimatedScroll() {
    val maxIndex = Integer.MAX_VALUE
    val maxOffset = Integer.MAX_VALUE
    animateScrollToItem(maxIndex, maxOffset)
}

@Composable
@Preview
fun BalanceGamePreview() {
    WithpeaceTheme {
        BalanceGameScreen(
            lazyListState = rememberLazyListState(),
            pagerState = PagerState { 0 },
            onClickBackButton = {},
            onClickBeforeDay = {},
            uiState = BalanceGameUIState.Success(listOf()),
            onSelectA = {},
            onSelectB = {},
            onClickAfterDay = {},
            onClickRegisterButton = {},
            onTextChanged = {},
            onReportComment = { _, _ -> },
            comment = "",
        )

    }
}
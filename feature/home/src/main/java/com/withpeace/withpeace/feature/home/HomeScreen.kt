package com.withpeace.withpeace.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.snackbar.SnackbarState
import com.withpeace.withpeace.core.designsystem.ui.snackbar.SnackbarType
import com.withpeace.withpeace.core.designsystem.util.dropShadow
import com.withpeace.withpeace.core.ui.analytics.TrackScreenViewEvent
import com.withpeace.withpeace.core.ui.bookmark.BookmarkButton
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.feature.home.filtersetting.FilterBottomSheet
import com.withpeace.withpeace.feature.home.uistate.HomeUiEvent
import com.withpeace.withpeace.feature.home.uistate.PolicyFiltersUiModel
import com.withpeace.withpeace.feature.home.uistate.YouthPolicyUiModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    onShowSnackBar: (message: String) -> Unit = {},
    onNavigationSnackBar: (message: String) -> Unit  = {},
    viewModel: HomeViewModel = hiltViewModel(),
    onPolicyClick: (String) -> Unit,
) {
    val youthPolicyPagingData = viewModel.youthPolicyPagingFlow.collectAsLazyPagingItems()
    val selectedFilterUiState = viewModel.selectingFilters.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect {
            when (it) {
                HomeUiEvent.BookmarkSuccess -> {
                    onNavigationSnackBar("관심 목록에 추가되었습니다.")
                }

                HomeUiEvent.BookmarkFailure -> {
                    onShowSnackBar("찜하기에 실패했습니다. 다시 시도해주세요.")
                }

                HomeUiEvent.UnBookmarkSuccess -> {
                    onShowSnackBar("관심목록에서 삭제되었습니다.")
                }
            }
        }

    }
    HomeScreen(
        youthPolicies = youthPolicyPagingData,
        selectedFilterUiState = selectedFilterUiState.value,
        onDismissRequest = viewModel::onCancelFilter,
        onClassificationCheckChanged = viewModel::onCheckClassification,
        onRegionCheckChanged = viewModel::onCheckRegion,
        onFilterAllOff = viewModel::onFilterAllOff,
        onSearchWithFilter = viewModel::onCompleteFilter,
        onCloseFilter = viewModel::onCancelFilter,
        onPolicyClick = onPolicyClick,
        onBookmarkClick = viewModel::bookmark
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    youthPolicies: LazyPagingItems<YouthPolicyUiModel>,
    selectedFilterUiState: PolicyFiltersUiModel,
    onDismissRequest: () -> Unit,
    onClassificationCheckChanged: (ClassificationUiModel) -> Unit,
    onRegionCheckChanged: (RegionUiModel) -> Unit,
    onFilterAllOff: () -> Unit,
    onSearchWithFilter: () -> Unit,
    onCloseFilter: () -> Unit,
    onPolicyClick: (String) -> Unit,
    onBookmarkClick: (id: String, isChecked: Boolean) -> Unit,

) {
    Column(modifier = modifier.fillMaxSize()) {
        HomeHeader(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            selectedFilterUiState = selectedFilterUiState,
            onClassificationCheckChanged = onClassificationCheckChanged,
            onRegionCheckChanged = onRegionCheckChanged,
            onFilterAllOff = onFilterAllOff,
            onSearchWithFilter = onSearchWithFilter,
            onCloseFilter = onCloseFilter,
        )
        HorizontalDivider(
            modifier = modifier.height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        when(youthPolicies.loadState.refresh) {
            is LoadState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = WithpeaceTheme.colors.MainPurple,
                    )
                }
            }

            is LoadState.Error -> {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        text = "네트워크 상태를 확인해주세요.",
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }

            is LoadState.NotLoading -> {
                if (youthPolicies.itemCount == 0) {
                    Column(
                        modifier = modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = modifier.height(213.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_no_result),
                            contentDescription = stringResource(
                                R.string.no_result,
                            ),
                        )
                        Spacer(modifier = modifier.height(24.dp))
                        Text(
                            text = "조건에 맞는 정책이 없어요.",
                            style = WithpeaceTheme.typography.semiBold16Sp,
                            color = WithpeaceTheme.colors.SystemBlack,
                            letterSpacing = 0.16.sp,
                            lineHeight = 21.sp,
                        )
                        Spacer(modifier = modifier.height(8.dp))
                        Text(
                            text = "필터 조건을 변경한 후 다시 시도해 보세요.",
                            style = WithpeaceTheme.typography.caption,
                            color = WithpeaceTheme.colors.SystemBlack,
                        )
                    }
                } else {
                    PolicyItems(
                        modifier,
                        youthPolicies,
                        onPolicyClick,
                        onBookmarkClick = onBookmarkClick,
                    )
                }
            }
        }
    }
    TrackScreenViewEvent(screenName = "home")
}

@Composable
private fun PolicyItems(
    modifier: Modifier,
    youthPolicies: LazyPagingItems<YouthPolicyUiModel>,
    onPolicyClick: (String) -> Unit,
    onBookmarkClick: (id: String, isChecked: Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = modifier.height(8.dp))
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag("home:policies"),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            items(
                count = youthPolicies.itemCount,
                key = youthPolicies.itemKey { it.id },
            ) {
                val youthPolicy = youthPolicies[it] ?: throw IllegalStateException()
                Spacer(modifier = modifier.height(8.dp))
                YouthPolicyCard(
                    modifier = modifier,
                    youthPolicy = youthPolicy,
                    onPolicyClick = onPolicyClick,
                    onBookmarkClick = onBookmarkClick,
                )
            }
            item {
                if (youthPolicies.loadState.append is LoadState.Loading) {
                    Column(
                        modifier = modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .background(
                                Color.Transparent,
                            ),
                    ) {
                        CircularProgressIndicator(
                            modifier.align(Alignment.CenterHorizontally),
                            color = WithpeaceTheme.colors.MainPurple,
                        )
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeHeader(
    modifier: Modifier,
    selectedFilterUiState: PolicyFiltersUiModel,
    onDismissRequest: () -> Unit,
    onClassificationCheckChanged: (ClassificationUiModel) -> Unit,
    onRegionCheckChanged: (RegionUiModel) -> Unit,
    onFilterAllOff: () -> Unit,
    onSearchWithFilter: () -> Unit,
    onCloseFilter: () -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetChildScrollState = rememberScrollState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(bottomSheetChildScrollState.canScrollBackward) {
        if (bottomSheetChildScrollState.value == 0) {
            bottomSheetChildScrollState.stopScroll(MutatePriority.PreventUserInput)
        }
    }

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
        Image(
            modifier = modifier
                .size(24.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    showBottomSheet = true
                },
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = stringResource(R.string.filter),
        )
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            dragHandle = null,
            onDismissRequest = {
                onDismissRequest()
                showBottomSheet = false
            },
            sheetState = sheetState,
        ) {
            FilterBottomSheet(
                modifier = modifier,
                scrollState = bottomSheetChildScrollState,
                selectedFilterUiState = selectedFilterUiState,
                onClassificationCheckChanged = onClassificationCheckChanged,
                onRegionCheckChanged = onRegionCheckChanged,
                onFilterAllOff = onFilterAllOff,
                onSearchWithFilter = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        showBottomSheet = false
                        onSearchWithFilter()
                    }
                },
                onCloseFilter = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        showBottomSheet = false
                        onCloseFilter()
                    }
                },
            )
        }
    }
}

@Composable
private fun YouthPolicyCard(
    modifier: Modifier,
    youthPolicy: YouthPolicyUiModel,
    onPolicyClick: (String) -> Unit,
    onBookmarkClick: (id: String, isChecked: Boolean) -> Unit,
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
                borderRadius = 10.dp,
            )
            .clickable {
                onPolicyClick(youthPolicy.id)
            },
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .background(WithpeaceTheme.colors.SystemWhite)
                .padding(16.dp),
        ) {
            val (
                title, content,
                region, ageRange, thumbnail, heart,
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
                color = WithpeaceTheme.colors.SystemBlack,
                style = WithpeaceTheme.typography.homePolicyTitle,
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
                color = WithpeaceTheme.colors.SystemBlack,
                style = WithpeaceTheme.typography.homePolicyContent,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
            BookmarkButton(
                isClicked = youthPolicy.isBookmarked,
                modifier = modifier.constrainAs(
                    heart,
                ) {
                    top.linkTo(content.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
                onClick = { isClicked ->
                    onBookmarkClick(youthPolicy.id, isClicked)
                },
            )

            Text(
                text = youthPolicy.region.name,
                color = WithpeaceTheme.colors.MainPurple,
                modifier = modifier
                    .constrainAs(
                        region,
                        constrainBlock = {
                            top.linkTo(heart.top)
                            start.linkTo(heart.end, margin = 8.dp)
                            bottom.linkTo(heart.bottom)
                        },
                    )
                    .background(
                        color = WithpeaceTheme.colors.SubPurple,
                        shape = RoundedCornerShape(size = 5.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = WithpeaceTheme.typography.homePolicyTag,
            )

            Text(
                text = youthPolicy.ageInfo,
                color = WithpeaceTheme.colors.SystemGray1,
                modifier = modifier
                    .constrainAs(
                        ageRange,
                        constrainBlock = {
                            top.linkTo(region.top)
                            start.linkTo(region.end, margin = 8.dp)
                            bottom.linkTo(region.bottom)
                        },
                    )
                    .background(
                        color = WithpeaceTheme.colors.SystemGray3,
                        shape = RoundedCornerShape(size = 5.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = WithpeaceTheme.typography.homePolicyTag,
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
                painter = painterResource(id = youthPolicy.classification.drawableResId),
                contentDescription = stringResource(R.string.policy_classification_image),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomePreview() {
    WithpeaceTheme {
        HomeScreen(
            youthPolicies =
            flowOf(
                PagingData.from(
                    listOf(
                        YouthPolicyUiModel(
                            id = "deterruisset",
                            title = "epicurei",
                            content = "interdum",
                            region = RegionUiModel.대구,
                            ageInfo = "quo",
                            classification = ClassificationUiModel.JOB,
                            isBookmarked = false,
                        ),
                    ),
                ),
            ).collectAsLazyPagingItems(),
            selectedFilterUiState = PolicyFiltersUiModel(),
            onDismissRequest = { },
            onClassificationCheckChanged = {},
            onRegionCheckChanged = {},
            onFilterAllOff = {},
            onSearchWithFilter = {},
            onCloseFilter = {},
            onPolicyClick = {},
            onBookmarkClick = { id, isChecked->}
        )
    }
}

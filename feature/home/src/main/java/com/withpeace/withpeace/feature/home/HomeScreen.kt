package com.withpeace.withpeace.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.stopScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.analytics.TrackScreenViewEvent
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.core.ui.policy.filtersetting.FilterBottomSheet
import com.withpeace.withpeace.core.ui.policy.filtersetting.PolicyFiltersUiModel
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel
import com.withpeace.withpeace.feature.home.uistate.HotPolicyUiState
import com.withpeace.withpeace.feature.home.uistate.RecentPostsUiState
import com.withpeace.withpeace.feature.home.uistate.RecommendPolicyUiState
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    onShowSnackBar: (message: String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigationSnackBar: (message: String) -> Unit  = {},
    onPolicyClick: (String) -> Unit,
    onPostClick: (PostTopicUiModel) -> Unit,
) {
    val selectingFilterUiState = viewModel.selectingFilters.collectAsStateWithLifecycle()
    val recentPosts = viewModel.recentPostsUiState.collectAsStateWithLifecycle()
    val hotPolicies = viewModel.hotPolicyUiState.collectAsStateWithLifecycle()
    val recommendPolicies = viewModel.recommendPolicyUiState.collectAsStateWithLifecycle()
    val completedFilterUiState = viewModel.completedFilters.collectAsStateWithLifecycle()
    HomeScreen(
        selectedFilterUiState = selectingFilterUiState.value,
        onClassificationCheckChanged = viewModel::onCheckClassification,
        onRegionCheckChanged = viewModel::onCheckRegion,
        onFilterAllOff = viewModel::onFilterAllOff,
        onSearchWithFilter = viewModel::onCompleteFilter,
        onCloseFilter = viewModel::onCancelFilter,
        onDismissRequest = viewModel::onCancelFilter,
        recentPosts = recentPosts.value,
        onPostClick = onPostClick,
        onPolicyClick = onPolicyClick,
        hotPolicyUiState = hotPolicies.value,
        recommendPolicyUiState = recommendPolicies.value,
        completedFilterState = completedFilterUiState.value,
    )
}

@Composable
fun HomeScreen(
    recentPosts: RecentPostsUiState,
    hotPolicyUiState: HotPolicyUiState,
    recommendPolicyUiState: RecommendPolicyUiState,
    modifier: Modifier = Modifier,
    selectedFilterUiState: PolicyFiltersUiModel,
    onDismissRequest: () -> Unit,
    onClassificationCheckChanged: (ClassificationUiModel) -> Unit,
    onRegionCheckChanged: (RegionUiModel) -> Unit,
    onFilterAllOff: () -> Unit,
    onSearchWithFilter: () -> Unit,
    onCloseFilter: () -> Unit,
    onPostClick: (PostTopicUiModel) -> Unit,
    onPolicyClick: (String) -> Unit,
    completedFilterState: PolicyFiltersUiModel,
) {
    Column(modifier = modifier.fillMaxSize()) {
        HomeHeader(
            modifier = modifier,
        )
        HorizontalDivider(
            modifier = modifier.height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        ScrollSection(
            recentPosts = recentPosts,
            selectedFilterUiState = selectedFilterUiState,
            onDismissRequest = onDismissRequest,
            onClassificationCheckChanged = onClassificationCheckChanged,
            onRegionCheckChanged = onRegionCheckChanged,
            onFilterAllOff = onFilterAllOff,
            onSearchWithFilter = onSearchWithFilter,
            onCloseFilter = onCloseFilter,
            onPostClick = onPostClick,
            onPolicyClick = onPolicyClick,
            hotPolicyUiState = hotPolicyUiState,
            recommendPolicyUiState = recommendPolicyUiState,
            completedFilterState = completedFilterState,
        )

    }
    TrackScreenViewEvent(screenName = "home")
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ScrollSection(
    onPolicyClick: (String) -> Unit,
    recentPosts: RecentPostsUiState,
    modifier: Modifier = Modifier,
    selectedFilterUiState: PolicyFiltersUiModel,
    onDismissRequest: () -> Unit,
    onClassificationCheckChanged: (ClassificationUiModel) -> Unit,
    onRegionCheckChanged: (RegionUiModel) -> Unit,
    onFilterAllOff: () -> Unit,
    onSearchWithFilter: () -> Unit,
    onCloseFilter: () -> Unit,
    onPostClick: (PostTopicUiModel) -> Unit,
    hotPolicyUiState: HotPolicyUiState,
    recommendPolicyUiState: RecommendPolicyUiState,
    completedFilterState: PolicyFiltersUiModel,
) {
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
                FlowRow(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_filter),
                        modifier = modifier
                            .background(
                                color = WithpeaceTheme.colors.SubPurple,
                                shape = CircleShape,
                            )
                            .padding(4.dp)
                            .size(16.dp)
                            .clickable {
                                showBottomSheet = true
                            },
                        contentDescription = "",
                    )
                    List(completedFilterState.classifications.size) { //TODO("데이터 변경")
                        Spacer(modifier = modifier.width(8.dp))
                        Text(
                            text = "#${stringResource(id = completedFilterState.classifications[it].stringResId)}",
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
                    List(completedFilterState.regions.size) { //TODO("데이터 변경")
                        Spacer(modifier = modifier.width(8.dp))
                        Text(
                            text = "#${completedFilterState.regions[it].name}",
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
                if (hotPolicyUiState is HotPolicyUiState.Success) {
                    items(hotPolicyUiState.policies.size) {
                        val hotPolicy = hotPolicyUiState.policies[it]
                        Column(
                            modifier.clickable {
                                onPolicyClick(hotPolicy.id)
                            },
                        ) {
                            Image(
                                modifier = modifier
                                    .size(140.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                painter = painterResource(id = hotPolicy.classification.drawableResId),
                                contentDescription = "",
                            )
                            Spacer(modifier = modifier.height(8.dp))
                            Text(
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2,
                                modifier = modifier.width(140.dp),
                                text = hotPolicy.title,
                                style = WithpeaceTheme.typography.caption,
                                color = WithpeaceTheme.colors.SnackbarBlack,
                            )
                        }
                    }
                } else {
                    //TODO 실패 UI
                    items(6) {
                        Column(
                            modifier.clickable {
                                onPolicyClick("R2024092726752")
                            },
                        ) {
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
                if (recommendPolicyUiState is RecommendPolicyUiState.Success) {
                    items(recommendPolicyUiState.policies.size) {
                        val recommentPolicy = recommendPolicyUiState.policies[it]
                        Column(
                            modifier.clickable {
                                onPolicyClick(recommentPolicy.id)
                            },
                        ) {
                            Image(
                                modifier = modifier
                                    .size(140.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                painter = painterResource(id = recommentPolicy.classification.drawableResId),
                                contentDescription = "",
                            )
                            Spacer(modifier = modifier.height(8.dp))
                            Text(
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2,
                                modifier = modifier.width(140.dp),
                                text = recommentPolicy.title,
                                style = WithpeaceTheme.typography.caption,
                                color = WithpeaceTheme.colors.SnackbarBlack,
                            )
                        }
                    }
                } else {
                    //TODO 실패 UI
                    items(6) {
                        Column(
                            modifier.clickable {
                                onPolicyClick("R2024092726752")
                            },
                        ) {
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
        if (recentPosts is RecentPostsUiState.Success) {
            items(PostTopicUiModel.entries.size) {
                val postTopic = PostTopicUiModel.entries[it]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .padding(start = 24.dp, end = 17.dp)
                        .clickable {
                            onPostClick(postTopic)
                        },
                ) {
                    Text(
                        text = stringResource(id = PostTopicUiModel.entries[it].textResId) + "게시판",
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SnackbarBlack,
                    )
                    Spacer(modifier = modifier.width(16.dp))
                    Text(
                        color = WithpeaceTheme.colors.SystemGray1,
                        style = WithpeaceTheme.typography.caption,
                        text = recentPosts.recentPosts.find { postTopic == it.type }?.title ?: "-",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
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
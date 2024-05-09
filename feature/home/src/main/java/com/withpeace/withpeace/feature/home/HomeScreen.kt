package com.withpeace.withpeace.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.util.dropShadow
import com.withpeace.withpeace.feature.home.filtersetting.FilterBottomSheet
import com.withpeace.withpeace.feature.home.filtersetting.uistate.ClassificationUiModel
import com.withpeace.withpeace.feature.home.uistate.PolicyFiltersUiModel
import com.withpeace.withpeace.feature.home.uistate.YouthPolicyUiModel

@Composable
fun HomeRoute(
    onShowSnackBar: (message: String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val youthPolicyPagingData = viewModel.youthPolicyPagingFlow.collectAsLazyPagingItems()
    val selectedFilterUiState = viewModel.selectingFilters.collectAsStateWithLifecycle()
    HomeScreen(
        youthPolicies = youthPolicyPagingData,
        selectedFilterUiState = selectedFilterUiState.value,
        onDismissRequest = viewModel::onCancelFilter,
        onClassificationCheckChange = viewModel::onCheckClassification
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    youthPolicies: LazyPagingItems<YouthPolicyUiModel>,
    selectedFilterUiState: PolicyFiltersUiModel,
    onDismissRequest: () -> Unit,
    onClassificationCheckChange: (ClassificationUiModel) -> Unit,
) {

    Column(modifier = modifier.fillMaxSize()) {
        HomeHeader(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            selectedFilterUiState = selectedFilterUiState,
            onClassificationCheckChange = onClassificationCheckChange
        )
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
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                userScrollEnabled = true,
                contentPadding = PaddingValues(bottom = 16.dp)
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
                    )
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
    onClassificationCheckChange: (ClassificationUiModel) -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
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
            dragHandle = null,
            onDismissRequest = {
                onDismissRequest()
                showBottomSheet = false
            },
            sheetState = sheetState,
            windowInsets = BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Bottom), // 바텀시트시 상태바의 색깔도 ScopeOut 색으로 바꾸기 위함
        ) {
            FilterBottomSheet(
                modifier = modifier,
                selectedFilterUiState = selectedFilterUiState,
                onClassificationCheckChange = onClassificationCheckChange
            )
        }
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

            Text(
                text = youthPolicy.region.name,
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
        // HomeScreen()
    }
}

// TODO("로딩 및 에러")
// TODO("로딩 뷰")

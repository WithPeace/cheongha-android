package com.withpeace.withpeace.feature.home.filtersetting

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.feature.home.R
import com.withpeace.withpeace.feature.home.filtersetting.uistate.FilterListUiState
import com.withpeace.withpeace.feature.home.uistate.PolicyFiltersUiModel

@Composable
fun FilterBottomSheet(
    scrollState: ScrollState,
    modifier: Modifier,
    selectedFilterUiState: PolicyFiltersUiModel,
    onClassificationCheckChanged: (ClassificationUiModel) -> Unit,
    onRegionCheckChanged: (RegionUiModel) -> Unit,
    onFilterAllOff: () -> Unit,
    onSearchWithFilter: () -> Unit,
    onCloseFilter: () -> Unit,
) {
    val filterListUiState =
        remember { mutableStateOf(FilterListUiState().getStateByFilterState(selectedFilterUiState)) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val footerHeight = remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current
    Box(
        modifier = modifier
            .heightIn(0.dp, screenHeight)
            .background(WithpeaceTheme.colors.SystemWhite),
    ) {
        FilterFooter(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .onSizeChanged {
                    footerHeight.value = with(localDensity) { it.height.toDp() }
                },
            onFilterAllOff = onFilterAllOff,
            onSearchWithFilter = onSearchWithFilter,
        )
        Column(
            modifier = modifier
                .align(Alignment.TopCenter)
                .padding(bottom = footerHeight.value),
        ) {
            FilterHeader(
                modifier = modifier,
                onCloseFilter = onCloseFilter,
            )
            ScrollableFilterSection(
                modifier = modifier,
                filterListUiState = filterListUiState.value,
                selectedFilterUiState = selectedFilterUiState,
                onClassificationCheckChanged = onClassificationCheckChanged,
                onRegionCheckChanged = onRegionCheckChanged,
                onClassificationMoreViewClick = {
                    filterListUiState.value =
                        filterListUiState.value.copy(isClassificationExpanded = !filterListUiState.value.isClassificationExpanded)
                },
                onRegionMoreViewClick = {
                    filterListUiState.value =
                        filterListUiState.value.copy(isRegionExpanded = !filterListUiState.value.isRegionExpanded)
                },
                scrollState = scrollState,
            )
        }
    }
}

@Composable
private fun FilterHeader(modifier: Modifier, onCloseFilter: () -> Unit) {
    Spacer(modifier = modifier.height(24.dp))
    Row(modifier = modifier.padding(horizontal = 16.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_filter_close),
            modifier = modifier.clickable {
                onCloseFilter()
            },
            contentDescription = stringResource(
                R.string.filter_close,
            ),
        )
        Text(
            text = stringResource(id = R.string.filter),
            modifier = modifier.padding(start = 8.dp),
            style = WithpeaceTheme.typography.title1,
            color = WithpeaceTheme.colors.SystemBlack,
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth()
            .background(WithpeaceTheme.colors.SystemGray3)
            .height(1.dp),
    )
}

@Composable
private fun ScrollableFilterSection(
    modifier: Modifier,
    filterListUiState: FilterListUiState,
    selectedFilterUiState: PolicyFiltersUiModel,
    onClassificationCheckChanged: (ClassificationUiModel) -> Unit,
    onRegionCheckChanged: (RegionUiModel) -> Unit,
    onClassificationMoreViewClick: () -> Unit,
    onRegionMoreViewClick: () -> Unit,
    scrollState: ScrollState,
) {
    val scrollSectionHeight = remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current
    val columnModifier = modifier.padding(horizontal = 24.dp)

    Column(
        modifier =
        if (scrollSectionHeight.value == 0.dp) columnModifier
            .onSizeChanged {
                if (!filterListUiState.isRegionExpanded && !filterListUiState.isClassificationExpanded) {
                    scrollSectionHeight.value = with(localDensity) { it.height.toDp() }
                }
            }
            .verticalScroll(scrollState)
        else columnModifier
            .height(scrollSectionHeight.value)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = modifier.height(16.dp))
        Text(
            text = stringResource(R.string.policy_classfication),
            style = WithpeaceTheme.typography.title2,
            color = WithpeaceTheme.colors.SystemBlack,
        )
        Spacer(modifier = modifier.height(16.dp))
        Column(
            modifier = modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium,
                ),
            ),
        ) {
            filterListUiState.getClassifications().forEach {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement =
                    Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = it.stringResId),
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemBlack,
                    )
                    Checkbox(
                        colors = CheckboxDefaults.colors(
                            checkedColor = WithpeaceTheme.colors.MainPurple,
                            uncheckedColor = WithpeaceTheme.colors.SystemGray2,
                            checkmarkColor = WithpeaceTheme.colors.SystemWhite,
                        ),
                        checked = selectedFilterUiState.classifications.contains(it),
                        onCheckedChange = { _ -> onClassificationCheckChanged(it) },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onClassificationMoreViewClick()
                },
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = if (filterListUiState.isClassificationExpanded) R.string.filter_fold else R.string.filter_expanded),
                color = WithpeaceTheme.colors.SystemGray1,
                style = WithpeaceTheme.typography.caption,
                modifier = modifier.padding(end = 4.dp),
            )
            Image(
                painterResource(id = if (filterListUiState.isClassificationExpanded) R.drawable.ic_filter_fold else R.drawable.ic_filter_expanded),
                contentDescription = stringResource(id = R.string.filter_expanded),
            )
        }
        Spacer(modifier = modifier.height(16.dp))
        HorizontalDivider(
            modifier = modifier.height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        Spacer(modifier = modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.region),
            style = WithpeaceTheme.typography.title2,
            color = WithpeaceTheme.colors.SystemBlack,
        )

        Spacer(modifier = modifier.height(16.dp))
        Column(
            modifier = modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium,
                ),
            ),
        ) {
            filterListUiState.getRegions().forEach { filterItem ->
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement =
                    Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = filterItem.name,
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemBlack,
                    )
                    Checkbox(
                        colors = CheckboxDefaults.colors(
                            checkedColor = WithpeaceTheme.colors.MainPurple,
                            uncheckedColor = WithpeaceTheme.colors.SystemGray2,
                            checkmarkColor = WithpeaceTheme.colors.SystemWhite,
                        ),
                        checked = selectedFilterUiState.regions.contains(filterItem),
                        onCheckedChange = { onRegionCheckChanged(filterItem) },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onRegionMoreViewClick()
                },
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(
                    if (filterListUiState.isRegionExpanded) R.string.filter_fold else R.string.filter_expanded,
                ),
                color = WithpeaceTheme.colors.SystemGray1,
                style = WithpeaceTheme.typography.caption,
                modifier = modifier.padding(end = 4.dp),
            )
            Image(
                painterResource(id = if (filterListUiState.isRegionExpanded) R.drawable.ic_filter_fold else R.drawable.ic_filter_expanded),
                contentDescription = stringResource(id = R.string.filter_expanded),
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun FilterFooter(
    modifier: Modifier,
    onFilterAllOff: () -> Unit,
    onSearchWithFilter: () -> Unit,
) {
    Column(modifier = modifier.wrapContentHeight()) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(WithpeaceTheme.colors.SystemGray3),
        )
        Row(
            modifier = modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                modifier = modifier,
                onClick = { onFilterAllOff() },
            ) {
                Text(
                    text = stringResource(R.string.filter_all_off),
                    color = WithpeaceTheme.colors.SystemGray1,
                    style = WithpeaceTheme.typography.body,
                )
            }
            TextButton(
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 32.dp),
                colors = ButtonColors(
                    containerColor = WithpeaceTheme.colors.MainPurple,
                    contentColor = WithpeaceTheme.colors.SystemWhite,
                    disabledContainerColor = WithpeaceTheme.colors.MainPurple,
                    disabledContentColor = WithpeaceTheme.colors.SystemWhite,
                ),
                shape = RoundedCornerShape(5.dp),
                onClick = { onSearchWithFilter() },
            ) {
                Text(text = stringResource(R.string.search))
            }
        }
    }
}

//TODO("최상단 스크롤 이벤트 완료 후 시트 닫히도록")

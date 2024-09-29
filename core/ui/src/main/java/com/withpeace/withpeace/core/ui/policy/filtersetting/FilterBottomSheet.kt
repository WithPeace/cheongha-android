package com.withpeace.withpeace.core.ui.policy.filtersetting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.R
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel

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
        remember {
            mutableStateOf(
                PolicyFiltersUiModel(
                    classifications = ClassificationUiModel.entries,
                    regions = RegionUiModel.entries,
                ),
            )
        }
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ScrollableFilterSection(
    modifier: Modifier,
    filterListUiState: PolicyFiltersUiModel,
    selectedFilterUiState: PolicyFiltersUiModel,
    onClassificationCheckChanged: (ClassificationUiModel) -> Unit,
    onRegionCheckChanged: (RegionUiModel) -> Unit,
    scrollState: ScrollState,
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = modifier.height(16.dp))
        Text(
            text = stringResource(R.string.policy_classification_image),
            style = WithpeaceTheme.typography.title2,
            color = WithpeaceTheme.colors.SnackbarBlack,
        )
        Spacer(modifier = modifier.height(16.dp))
        LazyRow {
            items(3) {
                val filterItem = filterListUiState.classifications[it]
                if (selectedFilterUiState.classifications.contains(filterItem)) {
                    TextButton(
                        colors = ButtonColors(
                            containerColor = WithpeaceTheme.colors.MainPurple,
                            contentColor = WithpeaceTheme.colors.SystemWhite,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = WithpeaceTheme.colors.SystemWhite,
                        ),
                        onClick = { onClassificationCheckChanged(filterItem) },
                        modifier = modifier
                            .padding(end = 8.dp),
                    ) {
                        Text(
                            style = WithpeaceTheme.typography.body,
                            text = stringResource(id = filterItem.stringResId),
                        )
                    }
                } else {
                    TextButton(
                        colors = ButtonColors(
                            containerColor = WithpeaceTheme.colors.SystemWhite,
                            contentColor = WithpeaceTheme.colors.SystemBlack,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = WithpeaceTheme.colors.SystemBlack,
                        ),
                        onClick = { onClassificationCheckChanged(filterItem) },
                        border = BorderStroke(
                            width = 1.dp,
                            color = WithpeaceTheme.colors.SystemGray2,
                        ),
                        modifier = modifier.padding(end = 8.dp),
                    ) {
                        Text(
                            style = WithpeaceTheme.typography.body,
                            text = stringResource(id = filterItem.stringResId),
                            color = WithpeaceTheme.colors.SystemBlack,
                        )
                    }
                }
            }
        }
        Spacer(modifier = modifier.height(8.dp))
        LazyRow {
            items(2) {
                val filterItem = filterListUiState.classifications[it + 3]
                if (selectedFilterUiState.classifications.contains(filterItem)) {
                    TextButton(
                        colors = ButtonColors(
                            containerColor = WithpeaceTheme.colors.MainPurple,
                            contentColor = WithpeaceTheme.colors.SystemWhite,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = WithpeaceTheme.colors.SystemWhite,
                        ),
                        onClick = { onClassificationCheckChanged(filterItem) },
                        modifier = modifier
                            .padding(end = 8.dp),
                    ) {
                        Text(
                            style = WithpeaceTheme.typography.body,
                            text = stringResource(id = filterItem.stringResId),
                        )
                    }
                } else {
                    TextButton(
                        colors = ButtonColors(
                            containerColor = WithpeaceTheme.colors.SystemWhite,
                            contentColor = WithpeaceTheme.colors.SystemBlack,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = WithpeaceTheme.colors.SystemBlack,
                        ),
                        onClick = { onClassificationCheckChanged(filterItem) },
                        border = BorderStroke(
                            width = 1.dp,
                            color = WithpeaceTheme.colors.SystemGray2,
                        ),
                        modifier = modifier.padding(end = 8.dp),
                    ) {
                        Text(
                            style = WithpeaceTheme.typography.body,
                            text = stringResource(id = filterItem.stringResId),
                            color = WithpeaceTheme.colors.SystemBlack,
                        )
                    }
                }
            }
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
            color = WithpeaceTheme.colors.SnackbarBlack,
        )

        Spacer(modifier = modifier.height(16.dp))
        FlowRow {
            filterListUiState.regions.dropLast(1).forEach { filterItem ->
                if (selectedFilterUiState.regions.contains(filterItem)) {
                    TextButton(
                        colors = ButtonColors(
                            containerColor = WithpeaceTheme.colors.MainPurple,
                            contentColor = WithpeaceTheme.colors.SystemWhite,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = WithpeaceTheme.colors.SystemWhite,
                        ),
                        onClick = { onRegionCheckChanged(filterItem) },
                        modifier = modifier
                            .padding(end = 8.dp),
                    ) {
                        Text(
                            style = WithpeaceTheme.typography.body,
                            text = filterItem.name,
                        )
                    }
                } else {
                    TextButton(
                        colors = ButtonColors(
                            containerColor = WithpeaceTheme.colors.SystemWhite,
                            contentColor = WithpeaceTheme.colors.SystemBlack,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = WithpeaceTheme.colors.SystemBlack,
                        ),
                        onClick = { onRegionCheckChanged(filterItem) },
                        border = BorderStroke(
                            width = 1.dp,
                            color = WithpeaceTheme.colors.SystemGray2,
                        ),
                        modifier = modifier.padding(end = 8.dp),
                    ) {
                        Text(
                            style = WithpeaceTheme.typography.body,
                            text = filterItem.name,
                            color = WithpeaceTheme.colors.SystemBlack,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
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


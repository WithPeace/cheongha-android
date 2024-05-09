package com.withpeace.withpeace.feature.home.filtersetting

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.feature.home.R
import com.withpeace.withpeace.feature.home.filtersetting.uistate.ClassificationUiModel
import com.withpeace.withpeace.feature.home.filtersetting.uistate.FilterListUiState
import com.withpeace.withpeace.feature.home.uistate.PolicyFiltersUiModel

@Composable
fun FilterBottomSheet(
    modifier: Modifier,
    selectedFilterUiState: PolicyFiltersUiModel,
    onClassificationCheckChange: (ClassificationUiModel) -> Unit,
) {
    val scrollState = rememberScrollState()
    val filterListUiState = remember { mutableStateOf(FilterListUiState()) }
    Column {
        Spacer(modifier = modifier.height(24.dp))
        Row(modifier = modifier.padding(horizontal = 16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_filter_close),
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
        ScrollableFilterSection(
            modifier = modifier,
            scrollState = scrollState,
            filterListUiState = filterListUiState.value,
            selectedFilterUiState = selectedFilterUiState,
            onClassificationCheckChange = onClassificationCheckChange,
            onClassificationMoreViewClick = {
                filterListUiState.value =
                    filterListUiState.value.copy(isClassificationExpanded = !filterListUiState.value.isClassificationExpanded)
            },
        )
    }
}

@Composable
private fun ScrollableFilterSection(
    modifier: Modifier,
    scrollState: ScrollState,
    filterListUiState: FilterListUiState,
    selectedFilterUiState: PolicyFiltersUiModel,
    onClassificationCheckChange: (ClassificationUiModel) -> Unit,
    onClassificationMoreViewClick: () -> Unit,
) {
    Column(
        modifier
            .padding(horizontal = 24.dp)
            .verticalScroll(state = scrollState),

        ) {
        Spacer(modifier = modifier.height(16.dp))
        Text(
            text = stringResource(R.string.policy_classfication),
            style = WithpeaceTheme.typography.title2,
            color = WithpeaceTheme.colors.SystemBlack,
        )

        Spacer(modifier = modifier.height(16.dp))
        Log.d("test", "test")
        filterListUiState.getClassifications().map { filterItem ->
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement =
                Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = filterItem.resId),
                    style = WithpeaceTheme.typography.body,
                    color = WithpeaceTheme.colors.SystemBlack,
                )
                Checkbox(
                    checked = selectedFilterUiState.classifications.contains(filterItem),
                    onCheckedChange = { onClassificationCheckChange(filterItem) },
                )
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
                text = stringResource(R.string.see_more),
                color = WithpeaceTheme.colors.SystemGray1,
                style = WithpeaceTheme.typography.caption,
                modifier = modifier.padding(end = 4.dp),
            )
            Image(
                painterResource(id = R.drawable.ic_more),
                contentDescription = stringResource(id = R.string.see_more),
            )
        }
    }
}
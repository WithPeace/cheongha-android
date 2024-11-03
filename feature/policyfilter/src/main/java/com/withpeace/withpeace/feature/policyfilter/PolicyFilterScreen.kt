package com.withpeace.withpeace.feature.policyfilter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.R
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.core.ui.policy.filtersetting.PolicyFiltersUiModel

@Composable
fun PolicyFilterRoute(
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
    viewModel: PolicyFilterViewModel = hiltViewModel(),
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
    val selectedFilterUiState = viewModel.selectingFilters.collectAsStateWithLifecycle()
    PolicyFilterScreen(
        filterListUiState = filterListUiState.value,
        onClassificationCheckChanged = {},
        selectedFilterUiState = selectedFilterUiState.value,
        onRegionCheckChanged = {},
    )

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PolicyFilterScreen(
    modifier: Modifier = Modifier,
    filterListUiState: PolicyFiltersUiModel,
    selectedFilterUiState: PolicyFiltersUiModel,
    onClassificationCheckChanged: (ClassificationUiModel) -> Unit,
    onRegionCheckChanged: (RegionUiModel) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite)
            .padding(horizontal = 24.dp)
            .verticalScroll(scrollState),
    ) {
        Spacer(modifier = modifier.height(18.dp))
        Text(
            text = "SKIP",
            modifier = modifier
                .background(
                    shape = RoundedCornerShape(50.dp), color = WithpeaceTheme.colors.SubPurple,
                )
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .align(Alignment.End),
            style =
            WithpeaceTheme.typography.caption,
            color = WithpeaceTheme.colors.MainPurple,
        )
        Spacer(modifier = modifier.height(24.dp))
        Text(
            "맞춤 정책 추천을 위해\n" +
                "아래의 정보를 입력해주세요!",
            textAlign = TextAlign.Center, modifier = modifier.align(Alignment.CenterHorizontally),
            color = WithpeaceTheme.colors.SnackbarBlack, style = WithpeaceTheme.typography.title1,
        )
        Spacer(modifier = modifier.height(40.dp))
        Text(
            text = "관심있는 정책분야를 선택해주세요!",
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
        Spacer(modifier = modifier.height(57.dp))
        TextButton(
            onClick = {},
            modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(9.dp),
                    color = WithpeaceTheme.colors.MainPurple,
                ),
        ) {
            Text(
                text = "가입 완료",
                color = WithpeaceTheme.colors.SystemWhite,
                style = WithpeaceTheme.typography.body,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PolicyFilterPreview() {
    WithpeaceTheme {
        PolicyFilterScreen(
            filterListUiState = PolicyFiltersUiModel(
                classifications = ClassificationUiModel.entries,
                regions = RegionUiModel.entries,
            ),
            selectedFilterUiState = PolicyFiltersUiModel(
                classifications = listOf(),
                regions = listOf(),
            ),
            onClassificationCheckChanged = {}, onRegionCheckChanged = {},
        )
    }
}
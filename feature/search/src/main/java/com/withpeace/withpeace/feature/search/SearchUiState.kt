package com.withpeace.withpeace.feature.search

import androidx.paging.PagingData
import com.withpeace.withpeace.core.ui.policy.YouthPolicyUiModel

sealed interface SearchUiState {
    data class SearchSuccess(
        val data: PagingData<YouthPolicyUiModel>
    ) : SearchUiState
    data object Initialized : SearchUiState
    data object NoSearchResult : SearchUiState
    data object Loading : SearchUiState
    data object SearchFailure : SearchUiState
}
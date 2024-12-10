package com.withpeace.withpeace.feature.search

sealed interface SearchUiState {
    data object SearchSuccess : SearchUiState
    data object Initialized : SearchUiState
    data object NoSearchResult : SearchUiState
    data object Loading : SearchUiState
    data object SearchFailure : SearchUiState
}
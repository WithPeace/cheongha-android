package com.withpeace.withpeace.feature.search

sealed interface SearchUiState {
    data object Initialized : SearchUiState
    data object PagingData : SearchUiState
    data object SearchFailure : SearchUiState
}
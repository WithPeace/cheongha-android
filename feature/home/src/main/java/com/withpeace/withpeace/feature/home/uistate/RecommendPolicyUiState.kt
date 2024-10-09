package com.withpeace.withpeace.feature.home.uistate

sealed interface RecommendPolicyUiState {
    data class Success(val policies: List<YouthPolicyUiModel>) : RecommendPolicyUiState
    data object Loading : RecommendPolicyUiState
    data object Failure : RecommendPolicyUiState
}
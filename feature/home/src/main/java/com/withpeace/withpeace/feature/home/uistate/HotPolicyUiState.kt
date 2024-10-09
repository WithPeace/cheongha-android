package com.withpeace.withpeace.feature.home.uistate

sealed interface HotPolicyUiState {
    data class Success(val policies: List<YouthPolicyUiModel>) : HotPolicyUiState
    data object Loading : HotPolicyUiState
    data object Failure : HotPolicyUiState
}
package com.withpeace.withpeace.feature.home.uistate

sealed interface YouthPolicyUiState {
    data class Success(val youthPolicyUiModel: YouthPolicyUiModel) : YouthPolicyUiState
    data object Loading : YouthPolicyUiState
    data object Failure : YouthPolicyUiState
}
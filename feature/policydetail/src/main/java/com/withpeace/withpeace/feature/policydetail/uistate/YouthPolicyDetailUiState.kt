package com.withpeace.withpeace.feature.policydetail.uistate

sealed interface YouthPolicyDetailUiState {
    data object Loading : YouthPolicyDetailUiState
    data class Success(val youthPolicyDetail: YouthPolicyDetailUiModel) : YouthPolicyDetailUiState
    data object Failure : YouthPolicyDetailUiState
}
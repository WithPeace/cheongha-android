package com.withpeace.withpeace.feature.policybookmarks.uistate

sealed interface BookmarkedPolicyUIState {
    data object Loading : BookmarkedPolicyUIState
    data class Success(val youthPolicies: List<BookmarkedYouthPolicyUiModel>) :
        BookmarkedPolicyUIState

    data object Failure : BookmarkedPolicyUIState
}
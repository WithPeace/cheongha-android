package com.withpeace.withpeace.feature.policydetail.uistate

sealed interface YouthPolicyDetailUiEvent {
    data object UnAuthorizedError : YouthPolicyDetailUiEvent
    data object ResponseError : YouthPolicyDetailUiEvent
    data object BookmarkSuccess: YouthPolicyDetailUiEvent
    data object UnBookmarkSuccess: YouthPolicyDetailUiEvent
    data object BookmarkFailure: YouthPolicyDetailUiEvent
}
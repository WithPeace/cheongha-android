package com.withpeace.withpeace.feature.policydetail.uistate

sealed interface YouthPolicyDetailUiEvent {
    data object UnAuthorizedError : YouthPolicyDetailUiEvent
    data object ResponseError : YouthPolicyDetailUiEvent
}
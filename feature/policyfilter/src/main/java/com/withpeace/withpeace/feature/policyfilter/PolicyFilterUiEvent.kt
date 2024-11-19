package com.withpeace.withpeace.feature.policyfilter

sealed interface PolicyFilterUiEvent {
    data object Success : PolicyFilterUiEvent
    data object Failure : PolicyFilterUiEvent
}
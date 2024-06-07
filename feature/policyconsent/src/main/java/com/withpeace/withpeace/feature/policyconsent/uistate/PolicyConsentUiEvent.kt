package com.withpeace.withpeace.feature.policyconsent.uistate

sealed interface PolicyConsentUiEvent {
    data object SuccessToNext : PolicyConsentUiEvent
    data object FailureToNext : PolicyConsentUiEvent
}
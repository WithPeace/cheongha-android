package com.withpeace.withpeace.feature.home.uistate

sealed interface HomeUiEvent {
    data object BookmarkSuccess : HomeUiEvent
    data object BookmarkFailure : HomeUiEvent
}
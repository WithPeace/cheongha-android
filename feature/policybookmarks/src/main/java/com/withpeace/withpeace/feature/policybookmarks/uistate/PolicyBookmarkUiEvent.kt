package com.withpeace.withpeace.feature.policybookmarks.uistate

sealed interface PolicyBookmarkUiEvent {
    data object UnBookmarkSuccess : PolicyBookmarkUiEvent
    data object BookmarkSuccess : PolicyBookmarkUiEvent
    data object BookmarkFailure : PolicyBookmarkUiEvent
}
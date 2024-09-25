package com.withpeace.withpeace.feature.policylist.uistate

sealed interface PolicyListUiEvent {
    data object BookmarkSuccess : PolicyListUiEvent
    data object BookmarkFailure : PolicyListUiEvent
    data object UnBookmarkSuccess : PolicyListUiEvent
}
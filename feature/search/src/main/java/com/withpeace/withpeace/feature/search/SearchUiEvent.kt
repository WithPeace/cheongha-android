package com.withpeace.withpeace.feature.search

sealed interface SearchUiEvent {
    data object BookmarkFailure : SearchUiEvent
    data object BookmarkSuccess : SearchUiEvent
    data object UnBookmarkSuccess : SearchUiEvent
}
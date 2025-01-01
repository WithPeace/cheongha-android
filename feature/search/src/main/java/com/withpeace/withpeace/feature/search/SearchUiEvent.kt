package com.withpeace.withpeace.feature.search

sealed interface SearchUiEvent {
    data object BookmarkFailure : SearchUiEvent
    data object BookmarkSuccess : SearchUiEvent
    data object SingleCharacteristicError: SearchUiEvent
    data object UnBookmarkSuccess : SearchUiEvent
}
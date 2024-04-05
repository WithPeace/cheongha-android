package com.withpeace.withpeace.feature.postlist

sealed interface PostListUiEvent {
    data object UnAuthorizedError : PostListUiEvent

    data object NetworkError : PostListUiEvent
}

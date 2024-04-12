package com.withpeace.withpeace.feature.postdetail

sealed interface PostDetailUiEvent {
    data object DeleteFailByNetworkError : PostDetailUiEvent
    data object DeleteFailByAuthorizationError : PostDetailUiEvent
    data object DeleteSuccess : PostDetailUiEvent
}

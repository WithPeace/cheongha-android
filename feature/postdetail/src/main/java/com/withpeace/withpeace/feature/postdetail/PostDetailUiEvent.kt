package com.withpeace.withpeace.feature.postdetail

sealed interface PostDetailUiEvent {
    data object DeleteFailByNetworkError : PostDetailUiEvent
    data object UnAuthorzied : PostDetailUiEvent
    data object DeleteSuccess : PostDetailUiEvent
}

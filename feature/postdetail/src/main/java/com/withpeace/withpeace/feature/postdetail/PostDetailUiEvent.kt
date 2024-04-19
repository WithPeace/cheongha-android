package com.withpeace.withpeace.feature.postdetail

sealed interface PostDetailUiEvent {
    data object DeleteFailByNetworkError : PostDetailUiEvent
    data object UnAuthorized : PostDetailUiEvent
    data object DeleteSuccess : PostDetailUiEvent
    data object RegisterCommentFailByNetwork : PostDetailUiEvent
    data object RegisterCommentSuccess : PostDetailUiEvent
}

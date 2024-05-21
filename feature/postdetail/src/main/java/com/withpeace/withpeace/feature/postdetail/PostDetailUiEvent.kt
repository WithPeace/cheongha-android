package com.withpeace.withpeace.feature.postdetail

sealed interface PostDetailUiEvent {
    data object DeleteFailByNetworkError : PostDetailUiEvent
    data object UnAuthorized : PostDetailUiEvent
    data class DeleteSuccess(val postId: Long) : PostDetailUiEvent
    data object RegisterCommentFailByNetwork : PostDetailUiEvent
    data object RegisterCommentSuccess : PostDetailUiEvent
    data object ReportPostSuccess : PostDetailUiEvent
    data object ReportPostFail : PostDetailUiEvent
    data object ReportCommentSuccess : PostDetailUiEvent
    data object ReportCommentFail : PostDetailUiEvent
    data object ReportPostDuplicated : PostDetailUiEvent
    data object ReportCommentDuplicated : PostDetailUiEvent
}

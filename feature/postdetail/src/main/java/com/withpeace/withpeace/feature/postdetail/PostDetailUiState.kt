package com.withpeace.withpeace.feature.postdetail

import com.withpeace.withpeace.core.ui.post.PostDetailUiModel

sealed interface PostDetailUiState {
    data object Init : PostDetailUiState
    data class Success(val postDetail: PostDetailUiModel) : PostDetailUiState
    data object FailByNetwork : PostDetailUiState
    data object NotFound : PostDetailUiState
}

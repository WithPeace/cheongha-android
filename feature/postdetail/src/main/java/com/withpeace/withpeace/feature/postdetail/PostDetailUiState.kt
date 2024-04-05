package com.withpeace.withpeace.feature.postdetail

import com.withpeace.withpeace.core.ui.post.PostDetailUiModel

sealed interface PostDetailUiState {
    data object Loading : PostDetailUiState
    data class Success(val postDetail: PostDetailUiModel) : PostDetailUiState
    data object Fail : PostDetailUiState
}

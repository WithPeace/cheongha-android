package com.withpeace.withpeace.feature.home.uistate

import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel

sealed interface RecentPostsUiState {
    data class Success(val recentPosts: List<RecentPostUiModel>) : RecentPostsUiState
    data object Loading : RecentPostsUiState
    data object Failure : RecentPostsUiState
}

data class RecentPostUiModel(
    val id: Long,
    val title: String,
    val type: PostTopicUiModel,
)
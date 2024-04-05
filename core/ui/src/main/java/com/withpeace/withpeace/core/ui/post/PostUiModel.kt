package com.withpeace.withpeace.core.ui.post

import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.ui.PostTopicUiState

data class PostUiModel(
    val postId: Long,
    val title: String,
    val content: String,
    val postTopic: PostTopicUiState,
    val createDate: Date,
    val postImageUrl: String?,
)

fun Post.toPostUiModel() =
    PostUiModel(
        postId = postId,
        title = title,
        content = content,
        postTopic = PostTopicUiState.create(postTopic),
        createDate = createDate,
        postImageUrl = postImageUrl,
    )

package com.withpeace.withpeace.core.ui.post

import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.ui.DateUiModel
import com.withpeace.withpeace.core.ui.toUiModel

data class PostUiModel(
    val postId: Long,
    val title: String,
    val content: String,
    val postTopic: PostTopicUiModel,
    val createDate: DateUiModel,
    val postImageUrl: String?,
)

fun Post.toPostUiModel() =
    PostUiModel(
        postId = postId,
        title = title,
        content = content,
        postTopic = postTopic.toUi(),
        createDate = createDate.toUiModel(),
        postImageUrl = postImageUrl,
    )

package com.withpeace.withpeace.core.ui.post

import com.withpeace.withpeace.core.domain.model.post.PostDetail
import com.withpeace.withpeace.core.ui.DateUiModel
import com.withpeace.withpeace.core.ui.toUiModel

data class PostDetailUiModel(
    val postUser: PostUserUiModel,
    val id: Long,
    val title: String,
    val content: String,
    val postTopic: PostTopicUiModel,
    val imageUrls: List<String>,
    val createDate: DateUiModel,
    val isMyPost: Boolean,
)

data class PostUserUiModel(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
)

fun PostDetail.toUiModel(currentUserId: Long): PostDetailUiModel = PostDetailUiModel(
    postUser = PostUserUiModel(
        id = user.id,
        name = user.name,
        profileImageUrl = user.profileImageUrl,
    ),
    id = id,
    title = title.value,
    content = content.value,
    postTopic = postTopic.toUi(),
    imageUrls = imageUrls,
    createDate = createDate.toUiModel(),
    isMyPost = user.id == currentUserId,
)

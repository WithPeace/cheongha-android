package com.withpeace.withpeace.core.network.di.response.post

import kotlinx.serialization.Serializable

@Serializable
data class PostDetailResponse(
    val postId: Long,
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String,
    val title: String,
    val content: String,
    val type: PostTopicResponse,
    val createDate: String,
    val postImageUrls: List<String>,
    val comments: List<CommentResponse>,
)

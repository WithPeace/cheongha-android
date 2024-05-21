package com.withpeace.withpeace.core.network.di.response.post

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val postId: Long,
    val title: String,
    val content: String,
    val type: PostTopicResponse,
    val postImageUrl: String? = null,
    val createDate: String,
    val commentCount: Int,
)

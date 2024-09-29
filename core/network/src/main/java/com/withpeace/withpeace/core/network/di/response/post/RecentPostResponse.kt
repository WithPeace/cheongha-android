package com.withpeace.withpeace.core.network.di.response.post

import kotlinx.serialization.Serializable

@Serializable
data class RecentPostResponse(
    val type: PostTopicResponse,
    val postId: Long,
    val title: String,
)

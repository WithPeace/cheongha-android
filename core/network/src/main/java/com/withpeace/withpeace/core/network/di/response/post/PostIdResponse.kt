package com.withpeace.withpeace.core.network.di.response.post

import kotlinx.serialization.Serializable

@Serializable
data class PostIdResponse(
    val postId: Long,
)

package com.withpeace.withpeace.core.network.di.response

import kotlinx.serialization.Serializable

@Serializable
data class PostIdResponse(
    val postId: Long,
)

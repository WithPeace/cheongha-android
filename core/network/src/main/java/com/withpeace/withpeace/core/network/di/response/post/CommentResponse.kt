package com.withpeace.withpeace.core.network.di.response.post

import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val commentId: Long,
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String,
    val content: String,
    val createDate: String,
)

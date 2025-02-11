package com.withpeace.withpeace.core.network.di.request

import kotlinx.serialization.Serializable

@Serializable
data class CommonCommentRequest(
    val targetType: String,
    val targetId: Long,
    val content: String,
)

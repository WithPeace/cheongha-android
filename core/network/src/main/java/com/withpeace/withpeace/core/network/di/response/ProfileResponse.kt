package com.withpeace.withpeace.core.network.di.response

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val email: String,
    val profileImageUrl: String,
    val nickname: String,
)

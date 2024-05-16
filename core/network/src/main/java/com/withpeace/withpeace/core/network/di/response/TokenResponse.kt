package com.withpeace.withpeace.core.network.di.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)

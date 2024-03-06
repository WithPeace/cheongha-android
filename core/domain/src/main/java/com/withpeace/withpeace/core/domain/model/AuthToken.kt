package com.withpeace.withpeace.core.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
)

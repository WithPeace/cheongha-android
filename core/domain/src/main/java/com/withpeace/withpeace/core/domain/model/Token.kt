package com.withpeace.withpeace.core.domain.model

data class Token(
    val accessToken: String,
    val refreshToken: String
)
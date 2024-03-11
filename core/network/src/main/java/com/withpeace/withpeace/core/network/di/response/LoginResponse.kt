package com.withpeace.withpeace.core.network.di.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("jwtTokenDto")
    val tokenResponse: TokenResponse,
    val role: String,
)

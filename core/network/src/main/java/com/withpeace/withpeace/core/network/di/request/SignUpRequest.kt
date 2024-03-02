package com.withpeace.withpeace.core.network.di.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email: String,
    val nickname: String,
    val deviceToken: String?,
)
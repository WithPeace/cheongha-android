package com.withpeace.withpeace.core.network.di.request

data class SignUpRequest(
    val email: String,
    val nickname: String,
    val deviceToken: String?,
)
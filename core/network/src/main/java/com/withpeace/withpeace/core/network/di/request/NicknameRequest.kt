package com.withpeace.withpeace.core.network.di.request

import kotlinx.serialization.Serializable

@Serializable
data class NicknameRequest(
    val nickname: String,
)
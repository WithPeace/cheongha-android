package com.withpeace.withpeace.core.network.di.response

import kotlinx.serialization.Serializable

@Serializable
data class ChangedProfileResponse(
    val nickname: String,
    val profileImageUrl: String,
)
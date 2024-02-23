package com.withpeace.withpeace.core.network.di.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val data: T,
    val error: String?,
)

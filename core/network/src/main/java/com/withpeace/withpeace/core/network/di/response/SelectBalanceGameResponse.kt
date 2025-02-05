package com.withpeace.withpeace.core.network.di.response

import kotlinx.serialization.Serializable

@Serializable
data class SelectBalanceGameResponse(
    val optionACount: Long,
    val optionBCount: Long,
)

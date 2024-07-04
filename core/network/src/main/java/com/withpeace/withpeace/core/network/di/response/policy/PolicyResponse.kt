package com.withpeace.withpeace.core.network.di.response.policy

import kotlinx.serialization.Serializable

@Serializable
data class PolicyResponse(
    val id: String,
    val title: String,
    val introduce: String,
    val classification: String,
    val region: String,
    val ageInfo: String
)

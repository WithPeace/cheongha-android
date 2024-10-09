package com.withpeace.withpeace.core.network.di.response.policy

import kotlinx.serialization.Serializable

@Serializable
data class UserPolicyFilterResponse(
    val classification: List<String>,
    val region: List<String>,
)

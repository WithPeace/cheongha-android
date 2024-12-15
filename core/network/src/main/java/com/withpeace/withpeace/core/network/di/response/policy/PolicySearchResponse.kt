package com.withpeace.withpeace.core.network.di.response.policy

import kotlinx.serialization.Serializable

@Serializable
data class PolicySearchResponse(
    val policies: List<PolicyResponse>,
    val totalCount: Int,
)

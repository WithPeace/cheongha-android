package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.network.di.response.YouthPolicyEntity

fun YouthPolicyEntity.toDomain(): YouthPolicy {
    return YouthPolicy(
        id = id,
        title = title ?: "",
        introduce = introduce ?: "",
        region = regionCode ?: "",
        policyClassification = classification?: "",
    )
}
package com.withpeace.withpeace.core.data.mapper.youthpolicy

import com.withpeace.withpeace.core.domain.model.policy.PolicyClassification
import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.domain.model.policy.PolicyRegion
import com.withpeace.withpeace.core.network.di.response.policy.UserPolicyFilterResponse

fun UserPolicyFilterResponse.toDomain(): PolicyFilters {
    return PolicyFilters(
        regions = region.map { name ->
            PolicyRegion.entries.find { it.name == name } ?: PolicyRegion.기타
        },
        classifications = classification.map { name ->
            PolicyClassification.entries.find { it.name == name } ?: PolicyClassification.ETC
        },
    )
}
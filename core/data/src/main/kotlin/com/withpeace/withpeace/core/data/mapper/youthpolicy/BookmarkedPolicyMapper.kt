package com.withpeace.withpeace.core.data.mapper.youthpolicy

import com.withpeace.withpeace.core.domain.model.policy.BookmarkedPolicy
import com.withpeace.withpeace.core.network.di.response.policy.BookmarkedPolicyResponse

fun BookmarkedPolicyResponse.toDomain(): BookmarkedPolicy {
    return BookmarkedPolicy(
        id = id,
        title = title,
        introduce = introduce,
        classification = classification.codeToPolicyClassification(),
        region = region.codeToRegion(),
        isActive = isActive,
    )
}
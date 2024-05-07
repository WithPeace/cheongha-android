package com.withpeace.withpeace.feature.home.navigation

import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.feature.home.YouthPolicyUiModel

fun YouthPolicy.toUiModel(): YouthPolicyUiModel {
    return YouthPolicyUiModel(
        id = id,
        title = title,
        content = introduce,
        region = region.name,
        ageInfo = ageInfo,
    )
}
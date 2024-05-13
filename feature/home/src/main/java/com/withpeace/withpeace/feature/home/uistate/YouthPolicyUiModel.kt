package com.withpeace.withpeace.feature.home.uistate

import com.withpeace.withpeace.core.domain.model.policy.PolicyClassification
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.feature.home.filtersetting.uistate.RegionUiModel
import com.withpeace.withpeace.feature.home.filtersetting.uistate.toDomain
import com.withpeace.withpeace.feature.home.filtersetting.uistate.toUiModel

data class YouthPolicyUiModel(
    val id: String,
    val title: String,
    val content: String,
    val region: RegionUiModel,
    val ageInfo: String,
)

fun YouthPolicy.toUiModel(): YouthPolicyUiModel {
    return YouthPolicyUiModel(
        id = id,
        title = title,
        content = introduce,
        region = region.toUiModel(),
        ageInfo = ageInfo,
    )
}

fun YouthPolicyUiModel.toDomain(): YouthPolicy {
    return YouthPolicy(
        id = id,
        title = title,
        introduce = content,
        region = region.toDomain(),
        policyClassification = PolicyClassification.JOB,
        ageInfo = ageInfo,
    )
}
package com.withpeace.withpeace.feature.policybookmarks.uistate

import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.core.ui.policy.toDomain
import com.withpeace.withpeace.core.ui.policy.toUiModel

data class YouthPolicyUiModel(
    val id: String,
    val title: String,
    val content: String,
    val region: RegionUiModel,
    val ageInfo: String,
    val classification: ClassificationUiModel,
    val isBookmarked: Boolean,
)

fun YouthPolicy.toUiModel(): YouthPolicyUiModel {
    return YouthPolicyUiModel(
        id = id,
        title = title,
        content = introduce,
        region = region.toUiModel(),
        ageInfo = ageInfo,
        classification = policyClassification.toUiModel(),
        isBookmarked = isBookmarked,
    )
}

fun YouthPolicyUiModel.toDomain(): YouthPolicy {
    return YouthPolicy(
        id = id,
        title = title,
        introduce = content,
        region = region.toDomain(),
        policyClassification = classification.toDomain(),
        ageInfo = ageInfo,
        isBookmarked = isBookmarked,
    )
}
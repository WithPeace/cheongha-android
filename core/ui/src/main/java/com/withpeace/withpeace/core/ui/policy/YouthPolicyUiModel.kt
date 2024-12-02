package com.withpeace.withpeace.core.ui.policy

import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy

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

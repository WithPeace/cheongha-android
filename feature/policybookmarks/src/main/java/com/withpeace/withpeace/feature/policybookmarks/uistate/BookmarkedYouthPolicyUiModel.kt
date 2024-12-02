package com.withpeace.withpeace.feature.policybookmarks.uistate

import com.withpeace.withpeace.core.domain.model.policy.BookmarkedPolicy
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.core.ui.policy.toDomain
import com.withpeace.withpeace.core.ui.policy.toUiModel

// isActive
data class BookmarkedYouthPolicyUiModel(
    val id: String,
    val title: String,
    val content: String,
    val region: RegionUiModel,
    val ageInfo: String,
    val classification: ClassificationUiModel,
    val isActive: Boolean,
    val isBookmarked: Boolean,
)

fun BookmarkedPolicy.toUiModel(): BookmarkedYouthPolicyUiModel {
    return BookmarkedYouthPolicyUiModel(
        id = id,
        title = title,
        content = introduce,
        region = region.toUiModel(),
        ageInfo = ageInfo,
        classification = classification.toUiModel(),
        isActive = isActive,
        isBookmarked = isBookmarked,
    )
}

fun BookmarkedYouthPolicyUiModel.toDomain(): BookmarkedPolicy {
    return BookmarkedPolicy(
        id = id,
        title = title,
        introduce = content,
        region = region.toDomain(),
        classification = classification.toDomain(),
        ageInfo = ageInfo,
        isActive = isActive,
        isBookmarked = isBookmarked,
    )
}
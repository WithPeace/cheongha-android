package com.withpeace.withpeace.feature.home.uistate

import androidx.annotation.DrawableRes
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
    @DrawableRes val classification: Int,
)

fun YouthPolicy.toUiModel(): YouthPolicyUiModel {
    return YouthPolicyUiModel(
        id = id,
        title = title,
        content = introduce,
        region = region.toUiModel(),
        ageInfo = ageInfo,
        classification = when (policyClassification) {
            PolicyClassification.JOB -> com.withpeace.withpeace.core.ui.R.drawable.ic_policy_job
            PolicyClassification.EDUCATION -> com.withpeace.withpeace.core.ui.R.drawable.ic_policy_eductaion
            PolicyClassification.RESIDENT -> com.withpeace.withpeace.core.ui.R.drawable.ic_policy_resident
            PolicyClassification.PARTICIPATION_AND_RIGHT -> com.withpeace.withpeace.core.ui.R.drawable.ic_policy_participation_right
            PolicyClassification.WELFARE_AND_CULTURE -> com.withpeace.withpeace.core.ui.R.drawable.ic_policy_welfare_culture
            else -> 0
        },
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
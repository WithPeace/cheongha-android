package com.withpeace.withpeace.core.ui.policy

import androidx.annotation.DrawableRes
import com.withpeace.withpeace.core.domain.model.policy.PolicyClassification
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.ui.R

data class YouthPolicyUiModel(
    val id: String,
    val title: String,
    val content: String,
    val region: RegionUiModel,
    val ageInfo: String,
    @DrawableRes val classification: Int,

    val residenceAndIncome: String,
    val education: String,
    val specialization: String,
    val additionalNotes: String,
    val participationRestrictions: String,
    val applicationProcess: String,
    val screeningAndAnnouncement: String,
    val applicationSite: String,
    val submissionDocuments: String
)

fun YouthPolicy.toUiModel(): YouthPolicyUiModel {
    return YouthPolicyUiModel(
        id = id,
        title = title,
        content = introduce,
        region = region.toUiModel(),
        ageInfo = ageInfo,
        classification = when (policyClassification) {
            PolicyClassification.JOB -> R.drawable.ic_policy_job
            PolicyClassification.EDUCATION -> R.drawable.ic_policy_eductaion
            PolicyClassification.RESIDENT -> R.drawable.ic_policy_resident
            PolicyClassification.PARTICIPATION_AND_RIGHT -> R.drawable.ic_policy_participation_right
            PolicyClassification.WELFARE_AND_CULTURE -> R.drawable.ic_policy_welfare_culture
            else -> 0
        },

        residenceAndIncome = residenceAndIncome,
        education = education,
        specialization = specialization,
        additionalNotes = additionalNotes,
        participationRestrictions = participationRestrictions,
        applicationProcess = applicationProcess,
        screeningAndAnnouncement = screeningAndAnnouncement,
        applicationSite = applicationSite,
        submissionDocuments = submissionDocuments
    )
}

fun YouthPolicyUiModel.toDomain(): YouthPolicy {
    return YouthPolicy(
        id = id,
        title = title,
        introduce = content,
        region = region.toDomain(),
        policyClassification = PolicyClassification.JOB, // Assuming this is a default or fixed value
        ageInfo = ageInfo,

        residenceAndIncome = residenceAndIncome,
        education = education,
        specialization = specialization,
        additionalNotes = additionalNotes,
        participationRestrictions = participationRestrictions,
        applicationProcess = applicationProcess,
        screeningAndAnnouncement = screeningAndAnnouncement,
        applicationSite = applicationSite,
        submissionDocuments = submissionDocuments
    )
}
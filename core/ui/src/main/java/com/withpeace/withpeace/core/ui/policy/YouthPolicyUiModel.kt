package com.withpeace.withpeace.core.ui.policy

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import com.withpeace.withpeace.core.domain.model.policy.PolicyClassification
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.ui.serializable.parseNavigationValue
import com.withpeace.withpeace.core.ui.serializable.toNavigationValue
import java.io.Serializable

@SuppressLint("SupportAnnotationUsage")
@kotlinx.serialization.Serializable
data class YouthPolicyUiModel(
    val id: String,
    val title: String,
    val content: String,
    val region: RegionUiModel,
    val ageInfo: String,
    @DrawableRes val classification: ClassificationUiModel,

    val applicationDetails: String,
    val residenceAndIncome: String,
    val education: String,
    val specialization: String,
    val additionalNotes: String,
    val participationRestrictions: String,
    val applicationProcess: String,
    val screeningAndAnnouncement: String,
    val applicationSite: String,
    val submissionDocuments: String,

    //추가 정보를 확인해 보세요
    val additionalUsefulInformation: String,
    val supervisingAuthority: String,
    val operatingOrganization: String,
    val businessRelatedReferenceSite1: String,
    val businessRelatedReferenceSite2: String,
): Serializable {
    companion object {
        fun toNavigationValue(value: YouthPolicyUiModel): String =
            value.toNavigationValue()

        fun parseNavigationValue(value: String): YouthPolicyUiModel =
            value.parseNavigationValue()
    }
}

fun YouthPolicy.toUiModel(): YouthPolicyUiModel {
    return YouthPolicyUiModel(
        id = id,
        title = title,
        content = introduce,
        region = region.toUiModel(),
        ageInfo = ageInfo,
        classification = policyClassification.toUiModel(),

        applicationDetails = applicationDetails,
        residenceAndIncome = residenceAndIncome,
        education = education,
        specialization = specialization,
        additionalNotes = additionalNotes,
        participationRestrictions = participationRestrictions,
        applicationProcess = applicationProcess,
        screeningAndAnnouncement = screeningAndAnnouncement,
        applicationSite = applicationSite,
        submissionDocuments = submissionDocuments,
        additionalUsefulInformation = additionalUsefulInformation,
        supervisingAuthority = supervisingAuthority,
        operatingOrganization = operatingOrganization,
        businessRelatedReferenceSite1 = businessRelatedReferenceSite1,
        businessRelatedReferenceSite2 = businessRelatedReferenceSite2,
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

        applicationDetails = applicationDetails,
        residenceAndIncome = residenceAndIncome,
        education = education,
        specialization = specialization,
        additionalNotes = additionalNotes,
        participationRestrictions = participationRestrictions,
        applicationProcess = applicationProcess,
        screeningAndAnnouncement = screeningAndAnnouncement,
        applicationSite = applicationSite,
        submissionDocuments = submissionDocuments,
        additionalUsefulInformation = additionalUsefulInformation,
        supervisingAuthority = supervisingAuthority,
        operatingOrganization = operatingOrganization,
        businessRelatedReferenceSite1 = businessRelatedReferenceSite1,
        businessRelatedReferenceSite2 = businessRelatedReferenceSite2,
    )
}
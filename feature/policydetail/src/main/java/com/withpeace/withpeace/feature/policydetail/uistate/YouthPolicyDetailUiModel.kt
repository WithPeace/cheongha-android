package com.withpeace.withpeace.feature.policydetail.uistate

import com.withpeace.withpeace.core.domain.model.policy.YouthPolicyDetail
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.toDomain
import com.withpeace.withpeace.core.ui.policy.toUiModel

data class YouthPolicyDetailUiModel(
    val id: String,
    val title: String,
    val content: String,
    val ageInfo: String,
    val classification: ClassificationUiModel,

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

    val isBookmarked: Boolean,
)

fun YouthPolicyDetail.toUiModel(): YouthPolicyDetailUiModel {
    return YouthPolicyDetailUiModel(
        id = id,
        title = title,
        content = introduce,
        ageInfo = ageInfo,
        classification = classification.toUiModel(),

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
        isBookmarked = isBookmarked,
    )
}

fun YouthPolicyDetailUiModel.toDomain(): YouthPolicyDetail {
    return YouthPolicyDetail(
        id = id,
        title = title,
        introduce = content,
        ageInfo = ageInfo,
        classification = classification.toDomain(),

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
        isBookmarked = isBookmarked,
    )
}
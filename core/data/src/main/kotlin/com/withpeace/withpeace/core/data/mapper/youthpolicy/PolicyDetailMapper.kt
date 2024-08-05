package com.withpeace.withpeace.core.data.mapper.youthpolicy

import com.withpeace.withpeace.core.domain.model.policy.YouthPolicyDetail
import com.withpeace.withpeace.core.network.di.response.policy.PolicyDetailResponse

internal fun PolicyDetailResponse.toDomain(): YouthPolicyDetail {
    return YouthPolicyDetail(
        id = id,
        title = title,
        introduce = introduce,
        ageInfo = ageInfo,
        classification = classification.codeToPolicyClassification(),

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
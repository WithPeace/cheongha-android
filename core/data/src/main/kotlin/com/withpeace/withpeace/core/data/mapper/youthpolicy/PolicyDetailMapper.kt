package com.withpeace.withpeace.core.data.mapper.youthpolicy

import com.withpeace.withpeace.core.domain.model.policy.YouthPolicyDetail
import com.withpeace.withpeace.core.network.di.response.policy.PolicyDetailResponse

fun PolicyDetailResponse.toDomain(): YouthPolicyDetail {
    return YouthPolicyDetail(
        id = id,
        title = title,
        introduce = introduce,
        classification = classification.codeToPolicyClassification(),

        applicationDetails = applicationDetails,
        applicationPeriodStatus = applicationPeriodStatus,
        operationPeriod = operationPeriod,
        ageInfo = ageInfo,
        residence = residence,
        income = income,
        education = education,
        specialization = specialization,
        additionalNotes = additionalNotes,
        participationRestrictions = participationRestrictions,
        applicationProcess = applicationProcess,
        screeningAndAnnouncement = screeningAndAnnouncement,
        applicationSite = applicationSite,
        submissionDocuments = submissionDocuments,
        etc = etc,
        managingInstitution = managingInstitution,
        operatingOrganization = operatingOrganization,
        referenceSite1 = referenceSite1,
        referenceSite2 = referenceSite2,
        isBookmarked = isBookmarked,
    )
}
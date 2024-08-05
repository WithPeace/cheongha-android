package com.withpeace.withpeace.core.domain.model.policy

data class YouthPolicyDetail(
    val id: String,
    val title: String,
    val introduce: String,
    val ageInfo: String,
    val classification: PolicyClassification,

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

    val additionalUsefulInformation: String,
    val supervisingAuthority: String,
    val operatingOrganization: String,
    val businessRelatedReferenceSite1: String,
    val businessRelatedReferenceSite2: String,
    val isBookmarked: Boolean,
)

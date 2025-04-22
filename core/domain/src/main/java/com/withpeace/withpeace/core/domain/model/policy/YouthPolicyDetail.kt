package com.withpeace.withpeace.core.domain.model.policy

data class YouthPolicyDetail(
    val id: String,
    val title: String,
    val introduce: String,
    val classification: PolicyClassification,

    val applicationDetails: String,
    val applicationPeriodStatus: String,
    val operationPeriod: String,
    val ageInfo: String,
    val residence: String,
    val income: String,
    val education: String,
    val specialization: String,
    val additionalNotes: String,
    val participationRestrictions: String,
    val applicationProcess: String,
    val screeningAndAnnouncement: String,
    val applicationSite: String,
    val submissionDocuments: String,
    val etc: String,
    val managingInstitution: String,
    val operatingOrganization: String,
    val referenceSite1: String,
    val referenceSite2: String,
    val isBookmarked: Boolean,
)
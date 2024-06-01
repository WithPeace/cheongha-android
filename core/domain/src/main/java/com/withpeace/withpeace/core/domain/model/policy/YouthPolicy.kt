package com.withpeace.withpeace.core.domain.model.policy

data class YouthPolicy(
    val id: String,
    val title: String,
    val introduce: String,
    val region: PolicyRegion,
    val policyClassification: PolicyClassification,
    val ageInfo: String,

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
)
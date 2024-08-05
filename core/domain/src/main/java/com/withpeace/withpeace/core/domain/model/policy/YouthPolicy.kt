package com.withpeace.withpeace.core.domain.model.policy

data class YouthPolicy(
    val id: String,
    val title: String,
    val introduce: String,
    val region: PolicyRegion,
    val policyClassification: PolicyClassification,
    val ageInfo: String,
    val isBookmarked: Boolean,
)

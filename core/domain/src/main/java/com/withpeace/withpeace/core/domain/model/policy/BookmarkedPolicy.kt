package com.withpeace.withpeace.core.domain.model.policy

data class BookmarkedPolicy(
    val id: String,
    val title: String,
    val introduce: String,
    val classification: PolicyClassification,
    val region: PolicyRegion,
    val isActive: Boolean,
)

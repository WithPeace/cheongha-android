package com.withpeace.withpeace.core.network.di.response.policy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookmarkedPolicyResponse(
    val id: String,
    val title: String,
    val introduce: String,
    val classification: String,
    val region: String,
    val ageInfo: String,
    val isActive: Boolean,
    @SerialName("isFavorite") val isBookmarked: Boolean,
)

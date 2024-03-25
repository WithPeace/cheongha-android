package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import com.withpeace.withpeace.core.network.di.response.ProfileResponse

fun ProfileResponse.toDomain(): ProfileInfo {
    return ProfileInfo(
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        email = email,
    )
}
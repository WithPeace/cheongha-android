package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.profile.Nickname
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import com.withpeace.withpeace.core.network.di.response.ProfileResponse

fun ProfileResponse.toDomain(): ProfileInfo {
    return ProfileInfo(
        nickname = Nickname(nickname),
        profileImageUrl = profileImageUrl,
        email = email,
    )
}
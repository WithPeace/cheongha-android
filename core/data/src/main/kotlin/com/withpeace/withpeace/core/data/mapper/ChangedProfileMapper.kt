package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.profile.ChangedProfile
import com.withpeace.withpeace.core.domain.model.profile.Nickname
import com.withpeace.withpeace.core.network.di.response.ChangedProfileResponse

fun ChangedProfileResponse.toDomain(): ChangedProfile {
    return ChangedProfile(
        nickname = Nickname.create(this.nickname),
        profileImageUrl = profileImageUrl,
    )
}

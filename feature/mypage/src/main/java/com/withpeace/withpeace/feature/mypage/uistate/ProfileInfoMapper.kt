package com.withpeace.withpeace.feature.mypage.uistate

import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo

internal fun ProfileInfo.toUiModel(): ProfileInfoUiModel {
    return ProfileInfoUiModel(
        nickname, profileImageUrl, email,
    )
}
package com.app.profileeditor

import com.app.profileeditor.uistate.ProfileUiModel
import com.withpeace.withpeace.core.domain.model.profile.ChangingProfileInfo

internal fun ProfileUiModel.toDomain(): ChangingProfileInfo {
    return ChangingProfileInfo(nickname, profileImage)
}
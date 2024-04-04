package com.withpeace.withpeace.feature.signup

import com.withpeace.withpeace.core.domain.model.SignUpInfo
import com.withpeace.withpeace.feature.signup.uistate.SignUpUiModel

fun SignUpUiModel.toDomain(): SignUpInfo {
    return SignUpInfo(nickname, profileImage)
}
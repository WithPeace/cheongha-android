package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo

class VerifyNicknameUseCase {
    operator fun invoke(nickname: String): Boolean {
        return ProfileInfo.validateNickname(nickname)
    }
}

package com.withpeace.withpeace.core.domain.model.profile

data class ChangingProfileInfo(val nickname: Nickname, val profileImage: String?) {

    companion object {
        operator fun invoke(nickname: String, profileImage: String?): ChangingProfileInfo {
            return ChangingProfileInfo(Nickname.create(nickname), profileImage)
        }
    }
}
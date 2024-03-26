package com.withpeace.withpeace.core.domain.model.profile

data class ChangingProfileInfo(val nickname: Nickname, val profileImage: String?) {

    fun isSameTo(nickname: String, profileImage: String?): Boolean {
        return Nickname.create(nickname) == this.nickname && profileImage == this.profileImage
    }

    companion object {
        operator fun invoke(nickname: String, profileImage: String?): ChangingProfileInfo {
            return ChangingProfileInfo(Nickname.create(nickname), profileImage)
        }
    }
}
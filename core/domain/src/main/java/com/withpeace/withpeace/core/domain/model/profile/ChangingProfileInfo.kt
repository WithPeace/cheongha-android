package com.withpeace.withpeace.core.domain.model.profile

data class ChangingProfileInfo(val nickname: String, val profileImage: String?) {
    fun isSameTo(nickname: String, profileImage: String?): Boolean {
        return nickname == this.nickname && profileImage == this.profileImage
    }
}
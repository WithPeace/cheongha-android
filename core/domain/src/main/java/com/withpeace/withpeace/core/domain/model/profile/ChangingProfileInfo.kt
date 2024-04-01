package com.withpeace.withpeace.core.domain.model.profile

data class ChangingProfileInfo(val nickname: String, val profileImage: String) {
    fun getChangingStatus(baseProfileInfo: ChangingProfileInfo): ProfileChangingStatus {
        return ProfileChangingStatus.getStatus(baseProfileInfo, this)
    }
}
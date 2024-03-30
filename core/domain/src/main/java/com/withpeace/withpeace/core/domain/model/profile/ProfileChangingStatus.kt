package com.withpeace.withpeace.core.domain.model.profile

sealed interface ProfileChangingStatus {
    data object OnlyNicknameChanging : ProfileChangingStatus
    data object OnlyImageChanging : ProfileChangingStatus
    data object AllChanging : ProfileChangingStatus

    companion object {
        fun getStatus(
            beforeProfile: ChangingProfileInfo,
            afterProfile: ChangingProfileInfo,
        ): ProfileChangingStatus {
            return if (beforeProfile.profileImage != afterProfile.profileImage
                && afterProfile.nickname != beforeProfile.nickname && afterProfile.profileImage != null
            ) {
                AllChanging
            } else if (afterProfile.profileImage != null && beforeProfile.profileImage != afterProfile.profileImage
            ) {
                OnlyImageChanging
            } else {
                OnlyNicknameChanging
            }
        }
    }
}
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
            return when {
                beforeProfile.profileImage != afterProfile.profileImage &&
                    afterProfile.nickname != beforeProfile.nickname &&
                    afterProfile.profileImage != null -> AllChanging

                afterProfile.profileImage != null &&
                    beforeProfile.profileImage != afterProfile.profileImage -> OnlyImageChanging

                else -> OnlyNicknameChanging
            }
        }
    }
}
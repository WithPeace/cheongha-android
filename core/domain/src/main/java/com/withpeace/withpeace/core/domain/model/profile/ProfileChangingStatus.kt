package com.withpeace.withpeace.core.domain.model.profile

sealed interface ProfileChangingStatus {
    data object OnlyNicknameChanging : ProfileChangingStatus

    data object OnlyImageChanging : ProfileChangingStatus

    data object AllChanging : ProfileChangingStatus

    data object Same : ProfileChangingStatus

    companion object {
        fun getStatus(
            beforeProfile: ChangingProfileInfo,
            afterProfile: ChangingProfileInfo,
        ): ProfileChangingStatus {
            return when {
                beforeProfile.profileImage != afterProfile.profileImage &&
                    afterProfile.nickname != beforeProfile.nickname -> AllChanging

                beforeProfile.nickname == afterProfile.nickname &&
                    beforeProfile.profileImage != afterProfile.profileImage -> OnlyImageChanging

                beforeProfile.nickname != afterProfile.nickname -> OnlyNicknameChanging

                else -> Same
            }
        }
    }
}
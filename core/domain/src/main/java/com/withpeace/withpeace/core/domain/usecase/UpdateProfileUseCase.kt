package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.ChangingProfileInfo
import com.withpeace.withpeace.core.domain.model.profile.ProfileChangingStatus
import com.withpeace.withpeace.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        beforeProfile: ChangingProfileInfo,
        afterProfile: ChangingProfileInfo,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Unit> {
        return when (ProfileChangingStatus.getStatus(beforeProfile, afterProfile)) {
            ProfileChangingStatus.AllChanging -> {
                userRepository.updateProfile(
                    afterProfile.nickname.value, afterProfile.profileImage!!,
                    onError = onError,
                )
            }
            ProfileChangingStatus.OnlyImageChanging -> {
                userRepository.updateProfileImage(
                    profileImage = afterProfile.profileImage!!,
                    onError = onError,
                )
            }
            ProfileChangingStatus.OnlyNicknameChanging -> {
                userRepository.updateNickname(afterProfile.nickname.value, onError = onError)
            }
        }
    }
}
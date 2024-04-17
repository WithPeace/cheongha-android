package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.profile.ChangedProfile
import com.withpeace.withpeace.core.domain.model.profile.ChangingProfileInfo
import com.withpeace.withpeace.core.domain.model.profile.ProfileChangingStatus
import com.withpeace.withpeace.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        beforeProfile: ChangingProfileInfo,
        afterProfile: ChangingProfileInfo,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<ChangedProfile> {
        return when (ProfileChangingStatus.getStatus(beforeProfile, afterProfile)) {
            ProfileChangingStatus.AllChanging -> {
                userRepository.updateProfile(
                    afterProfile.nickname, afterProfile.profileImage,
                    onError = onError,
                )
            }
            ProfileChangingStatus.OnlyImageChanging -> {
                userRepository.updateProfileImage(
                    profileImage = afterProfile.profileImage,
                    onError = onError,
                )
            }
            ProfileChangingStatus.OnlyNicknameChanging -> {
                userRepository.updateNickname(afterProfile.nickname, onError = onError)
            }

            ProfileChangingStatus.Same -> flow { onError(ClientError.ProfileNotChanged) }
        }
    }
}
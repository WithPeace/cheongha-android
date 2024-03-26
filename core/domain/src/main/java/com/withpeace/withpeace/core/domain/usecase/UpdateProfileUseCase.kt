package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.ChangingProfileInfo
import com.withpeace.withpeace.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        beforeProfile: ChangingProfileInfo,
        afterProfile: ChangingProfileInfo,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Unit> {
        println("test test test test")
        return if (beforeProfile.profileImage != afterProfile.profileImage
            && afterProfile.nickname != beforeProfile.nickname && afterProfile.profileImage != null
        ) { // 이미지 닉네임 둘 다 변경 되었을 때
            userRepository.updateProfile(
                afterProfile.nickname.value, afterProfile.profileImage,
                onError = onError,
            )
        } else if (afterProfile.profileImage != null && beforeProfile.profileImage != afterProfile.profileImage
        ) { // 이미지만 변경되었을 때
            userRepository.updateProfileImage(
                profileImage = afterProfile.profileImage,
                onError = onError,
            )
        } else { // 닉네임만 변경 되었을 때
            userRepository.updateNickname(afterProfile.nickname.value, onError = onError)
        }
    }
}
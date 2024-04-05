package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import com.withpeace.withpeace.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(onError: suspend (WithPeaceError) -> Unit): Flow<ProfileInfo> {
        return userRepository.getProfile(
            onError = onError,
        )
    }
}

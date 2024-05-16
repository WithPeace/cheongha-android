package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Long {
        return userRepository.getCurrentUserId()
    }
}

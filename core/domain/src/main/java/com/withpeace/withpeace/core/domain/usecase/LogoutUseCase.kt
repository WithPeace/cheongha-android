package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(onError: suspend (CheonghaError) -> Unit) = userRepository.logout(
        onError = onError,
    )
}
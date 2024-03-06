package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.AuthTokenRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authTokenRepository: AuthTokenRepository,
) {
    suspend operator fun invoke(
        email: String,
        nickname: String,
        onError: (String) -> Unit,
    ) = authTokenRepository.signUp(
        email = email,
        nickname = nickname,
        onError = onError,
    )
}

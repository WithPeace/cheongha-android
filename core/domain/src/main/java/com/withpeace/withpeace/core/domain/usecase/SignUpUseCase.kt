package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.TokenRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(
        email: String,
        nickname: String,
    ) = tokenRepository.signUp(
        email = email,
        nickname = nickname,
    )
}

package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.TokenRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class IsLoginUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(): Boolean {
        val token = tokenRepository.getAccessToken().firstOrNull()
        return token != null
    }
}

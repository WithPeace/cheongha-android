package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.AuthTokenRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class IsLoginUseCase @Inject constructor(
    private val authTokenRepository: AuthTokenRepository,
) {
    suspend operator fun invoke(): Boolean {
        val token = authTokenRepository.getAccessToken().firstOrNull()
        return token != null
    }
}

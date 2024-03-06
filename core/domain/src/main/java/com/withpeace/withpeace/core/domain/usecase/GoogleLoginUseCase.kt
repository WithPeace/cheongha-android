package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.AuthTokenRepository
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val authTokenRepository: AuthTokenRepository,
) {
    suspend operator fun invoke(
        idToken: String,
        onError: (String) -> Unit,
        onSuccess: () -> Unit,
    ) {
        authTokenRepository.getTokenByGoogle(
            idToken = idToken,
            onError = onError,
        ).collect { token ->
            authTokenRepository.updateAccessToken(token.accessToken)
            authTokenRepository.updateRefreshToken(token.refreshToken)
            onSuccess()
        }
    }
}

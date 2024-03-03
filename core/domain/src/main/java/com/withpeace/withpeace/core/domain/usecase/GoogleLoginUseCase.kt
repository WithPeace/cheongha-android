package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.Response
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(
        idToken: String,
    ): Flow<Response<Unit>> = flow {
        tokenRepository.googleLogin(idToken)
            .collect { result ->
                when (result) {
                    is Response.Failure -> emit(Response.Failure())
                    is Response.Success -> {
                        tokenRepository.updateAccessToken(result.data.accessToken)
                        tokenRepository.updateRefreshToken(result.data.refreshToken)
                        emit(Response.Success(Unit))
                    }
                }
            }
    }
}

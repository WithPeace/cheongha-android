package com.withpeace.withpeace.core.data.repository

import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnFailure
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.datastore.dataStore.TokenPreferenceDataSource
import com.withpeace.withpeace.core.domain.model.Token
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import com.withpeace.withpeace.core.network.di.request.SignUpRequest
import com.withpeace.withpeace.core.network.di.service.AuthService
import com.withpeace.withpeace.core.network.di.service.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultTokenRepository
    @Inject
    constructor(
        private val tokenPreferenceDataSource: TokenPreferenceDataSource,
        private val loginService: LoginService,
        private val authService: AuthService,
    ) : TokenRepository {
        override fun getAccessToken(): Flow<String?> {
            return tokenPreferenceDataSource.accessToken
        }

        override fun getRefreshToken(): Flow<String?> {
            return tokenPreferenceDataSource.refreshToken
        }

        override suspend fun updateAccessToken(accessToken: String) {
            tokenPreferenceDataSource.updateAccessToken(accessToken)
        }

        override suspend fun updateRefreshToken(refreshToken: String) {
            tokenPreferenceDataSource.updateRefreshToken(refreshToken)
        }

        override suspend fun signUp(
            email: String,
            nickname: String,
            deviceToken: String?,
        ) {
            authService.signUp(
                SignUpRequest(
                    email = email,
                    nickname = nickname,
                    deviceToken = deviceToken,
                ),
            )
        }

        override fun googleLogin(
            idToken: String,
            onError: (String?) -> Unit,
        ): Flow<Token> =
            flow {
                loginService.googleLogin(AUTHORIZATION_FORMAT.format(idToken))
                    .suspendMapSuccess {
                        emit(data.toDomain())
                        updateAccessToken(data.accessToken)
                        updateRefreshToken(data.refreshToken)
                    }.suspendOnFailure {
                        onError(messageOrNull)
                    }
            }.flowOn(Dispatchers.IO)

        companion object {
            private const val AUTHORIZATION_FORMAT = "Bearer %s"
        }
    }

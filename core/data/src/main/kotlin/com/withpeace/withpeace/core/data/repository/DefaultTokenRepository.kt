package com.withpeace.withpeace.core.data.repository

import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.datastore.dataStore.TokenPreferenceDataSource
import com.withpeace.withpeace.core.domain.model.Token
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import com.withpeace.withpeace.core.network.di.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class DefaultTokenRepository @Inject constructor(
    private val tokenPreferenceDataSource: TokenPreferenceDataSource,
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

    override fun googleLogin(onError: (String?) -> Unit): Flow<Token> = flow {
        authService.googleLogin()
            .suspendMapSuccess {
                emit(data.toDomain())
                updateAccessToken(data.accessToken)
                updateRefreshToken(data.refreshToken)
            }.suspendOnError {
                onError(message())
            }
    }.flowOn(Dispatchers.IO)
}
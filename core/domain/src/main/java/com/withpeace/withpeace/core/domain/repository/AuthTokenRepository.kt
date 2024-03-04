package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.AuthToken
import kotlinx.coroutines.flow.Flow

interface AuthTokenRepository {

    fun getAccessToken(): Flow<String?>

    fun getRefreshToken(): Flow<String?>

    suspend fun updateAccessToken(accessToken: String)

    suspend fun updateRefreshToken(refreshToken: String)

    suspend fun signUp(
        email: String,
        nickname: String,
        onError: (String) -> Unit,
    ): Flow<AuthToken>

    fun getTokenByGoogle(
        idToken: String,
        onError: (String) -> Unit,
    ): Flow<AuthToken>
}

package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.Token
import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    fun getAccessToken(): Flow<String?>

    fun getRefreshToken(): Flow<String?>

    suspend fun updateAccessToken(accessToken: String)

    suspend fun updateRefreshToken(refreshToken: String)

    suspend fun signUp(
        onError: (message: String?) -> Unit,
        email: String,
        nickname: String,
        deviceToken: String?,
    ): Flow<Token>

    fun googleLogin(
        idToken: String,
        onError: (message: String?) -> Unit,
    ): Flow<Token>
}
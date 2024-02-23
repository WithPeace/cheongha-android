package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.Token
import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    fun getAccessToken(): Flow<String?>

    fun getRefreshToken(): Flow<String?>

    suspend fun updateAccessToken(accessToken: String)

    suspend fun updateRefreshToken(refreshToken: String)

    fun googleLogin(onError: (message: String?) -> Unit): Flow<Token>
}
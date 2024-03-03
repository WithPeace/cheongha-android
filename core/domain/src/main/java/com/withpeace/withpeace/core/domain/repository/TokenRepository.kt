package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.Response
import com.withpeace.withpeace.core.domain.model.Token
import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    fun getAccessToken(): Flow<String?>

    fun getRefreshToken(): Flow<String?>

    suspend fun updateAccessToken(accessToken: String)

    suspend fun updateRefreshToken(refreshToken: String)

    suspend fun signUp(
        email: String,
        nickname: String,
    ): Flow<Response<Token>>

    fun googleLogin(
        idToken: String,
    ): Flow<Response<Token>>
}

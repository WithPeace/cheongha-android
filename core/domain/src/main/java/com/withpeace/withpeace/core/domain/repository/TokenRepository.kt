package com.withpeace.withpeace.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    suspend fun isLogin(): Boolean

    suspend fun signUp(
        email: String,
        nickname: String,
        onError: (String) -> Unit,
    ): Flow<Unit>

    fun getTokenByGoogle(
        idToken: String,
        onError: (String) -> Unit,
    ): Flow<Unit>
}

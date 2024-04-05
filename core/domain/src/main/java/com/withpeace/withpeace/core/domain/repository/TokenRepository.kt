package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.role.Role
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    suspend fun isLogin(): Boolean

    fun getTokenByGoogle(
        idToken: String,
        onError: (String) -> Unit,
    ): Flow<Role>
}

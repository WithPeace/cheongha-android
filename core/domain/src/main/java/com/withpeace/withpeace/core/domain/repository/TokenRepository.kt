package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.Token
import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    fun googleLogin(onError: (message: String?) -> Unit): Flow<Token>
}
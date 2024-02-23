package com.withpeace.withpeace.core.data.repository

import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.domain.model.Token
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import com.withpeace.withpeace.core.network.di.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class DefaultTokenRepository @Inject constructor(
    private val authService: AuthService
) : TokenRepository {

    override fun googleLogin(onError: (String?) -> Unit): Flow<Token> = flow {
        authService.googleLogin()
            .suspendMapSuccess {
                emit(data.toDomain())
            }.suspendOnError {
                onError(message())
            }
    }.flowOn(Dispatchers.IO)
}
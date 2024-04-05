package com.withpeace.withpeace.core.data.repository

import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnFailure
import com.withpeace.withpeace.core.data.mapper.roleToDomain
import com.withpeace.withpeace.core.datastore.dataStore.token.TokenPreferenceDataSource
import com.withpeace.withpeace.core.datastore.dataStore.user.UserPreferenceDataSource
import com.withpeace.withpeace.core.domain.model.role.Role
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import com.withpeace.withpeace.core.network.di.response.LoginResponse
import com.withpeace.withpeace.core.network.di.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultTokenRepository @Inject constructor(
    private val tokenPreferenceDataSource: TokenPreferenceDataSource,
    private val userPreferenceDataSource: UserPreferenceDataSource,
    private val authService: AuthService,
) : TokenRepository {
    override suspend fun isLogin(): Boolean {
        val token = tokenPreferenceDataSource.accessToken.firstOrNull()
        val userRole = userPreferenceDataSource.userRole.firstOrNull()
        return token != null && userRole?.roleToDomain() == Role.USER //TODO("토큰이 만료되었는지 확인 필요")
    }

    override fun getTokenByGoogle(
        idToken: String,
        onError: (String) -> Unit,
    ): Flow<Role> = flow {
        authService.googleLogin(AUTHORIZATION_FORMAT.format(idToken)).suspendMapSuccess {
            val data = this.data
            saveLocalLoginInfo(data)
            emit(data.role.roleToDomain())
        }.suspendOnFailure { onError(message()) }
    }.flowOn(Dispatchers.IO)

    private suspend fun saveLocalLoginInfo(data: LoginResponse) {
        tokenPreferenceDataSource.updateAccessToken(data.tokenResponse.accessToken)
        tokenPreferenceDataSource.updateRefreshToken(data.tokenResponse.refreshToken)
        userPreferenceDataSource.updateUserId(data.userId)
        userPreferenceDataSource.updateUserRole(data.role)
    }

    companion object {
        private const val AUTHORIZATION_FORMAT = "Bearer %s"
    }
}

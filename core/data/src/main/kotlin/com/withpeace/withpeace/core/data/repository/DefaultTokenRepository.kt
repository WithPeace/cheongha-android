package com.withpeace.withpeace.core.data.repository

import android.util.Log
import com.skydoves.sandwich.suspendMapSuccess
import com.withpeace.withpeace.core.analytics.AnalyticsEvent
import com.withpeace.withpeace.core.analytics.AnalyticsHelper
import com.withpeace.withpeace.core.data.analytics.event
import com.withpeace.withpeace.core.data.mapper.roleToDomain
import com.withpeace.withpeace.core.data.util.handleApiFailure
import com.withpeace.withpeace.core.datastore.dataStore.token.TokenPreferenceDataSource
import com.withpeace.withpeace.core.datastore.dataStore.user.UserPreferenceDataSource
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.role.Role
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import com.withpeace.withpeace.core.network.di.response.LoginResponse
import com.withpeace.withpeace.core.network.di.service.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultTokenRepository @Inject constructor(
    private val tokenPreferenceDataSource: TokenPreferenceDataSource,
    private val userPreferenceDataSource: UserPreferenceDataSource,
    private val authService: AuthService,
    private val analyticsHelper: AnalyticsHelper,
) : TokenRepository {
    override suspend fun isLogin(): Boolean {
        val token = tokenPreferenceDataSource.accessToken.firstOrNull()
        val userRole = userPreferenceDataSource.userRole.firstOrNull()
        return token != null && userRole?.roleToDomain() == Role.USER //TODO("토큰이 만료되었는지 확인 필요")
    }

    override fun getTokenByGoogle(
        idToken: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Role> = flow {
        authService.googleLogin(AUTHORIZATION_FORMAT.format(idToken)).suspendMapSuccess {
            val data = this.data
            saveLocalLoginInfo(data)
            emit(data.role.roleToDomain())
            analyticsHelper.event(AnalyticsEvent.Type.LOGIN)
        }.handleApiFailure(onError)
    }

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

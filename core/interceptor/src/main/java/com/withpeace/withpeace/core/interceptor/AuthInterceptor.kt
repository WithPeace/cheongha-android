package com.withpeace.withpeace.core.interceptor

import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.withpeace.withpeace.core.datastore.dataStore.token.TokenPreferenceDataSource
import com.withpeace.withpeace.core.network.di.response.TokenResponse
import com.withpeace.withpeace.core.network.di.service.AuthService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenPreferenceDataSource: TokenPreferenceDataSource,
    private val authService: AuthService,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // 일단 저장된 accessToken을 Header에 넣어준다
        val accessToken = runBlocking { tokenPreferenceDataSource.accessToken.firstOrNull() }
        var response = chain.getResponse(accessToken)
        var count = 1

        // Header에 넣어줬는데, 401이 뜬다면, RefreshToken 유무를 확인하고, accessToken을 재발급받아 다시 Header에 넣어준다.
        if (response.code == 401) {
            val refreshToken = runBlocking { tokenPreferenceDataSource.refreshToken.firstOrNull() }
            if (refreshToken != null) {
                while (count <= REQUEST_MAX_NUM) {
                    requestRefreshToken(
                        onSuccess = { data ->
                            tokenPreferenceDataSource.updateAccessToken(data.accessToken)
                            tokenPreferenceDataSource.updateRefreshToken(data.refreshToken)
                            response = chain.getResponse(data.accessToken)
                            count = 4
                        },
                        onFail = { count++ },
                    )
                }
            }
        }
        return response
    }

    private fun requestRefreshToken(
        onSuccess: suspend (TokenResponse) -> Unit,
        onFail: () -> Unit,
    ) {
        runBlocking {
            authService.refreshAccessToken()
                .suspendMapSuccess {
                    onSuccess(data)
                }.suspendOnError {
                    onFail()
                }
        }
    }

    private fun Interceptor.Chain.getResponse(accessToken: String?): Response {
        return this
            .proceed(
                request()
                    .newBuilder()
                    .addHeader(
                        ACCESS_TOKEN_HEADER,
                        TOKEN_FORMAT.format(accessToken),
                    ).build(),
            )
    }

    companion object {
        private const val ACCESS_TOKEN_HEADER = "Authorization"
        private const val TOKEN_FORMAT = "Bearer %s"
        private const val REQUEST_MAX_NUM = 3
    }
}

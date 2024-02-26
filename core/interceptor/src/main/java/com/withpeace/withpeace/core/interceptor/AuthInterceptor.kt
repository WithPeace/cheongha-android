package com.withpeace.withpeace.core.interceptor

import android.content.Context
import com.withpeace.withpeace.core.datastore.dataStore.TokenPreferenceDataSource
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    private val tokenPreferenceDataSource = EntryPointAccessors
        .fromApplication<AuthInterceptorEntryPoint>(context)
        .getTokenPreferenceDataSource()

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val accessToken = tokenPreferenceDataSource.accessToken.firstOrNull()
        val tokenAddedRequest = chain.request().putAccessToken(accessToken)
        val response = chain.proceed(tokenAddedRequest)

        if (response.isAccessTokenInvalid()) {
            val refreshToken = tokenPreferenceDataSource.refreshToken.firstOrNull()
            if (refreshToken == null /*|| 유효하지 않을 때*/) {
                // 로그인 화면
            } else {
                // refresh API 호출
                // API 실패시 로그인 화면 이동
                // 성공시 토큰을 넣어준다.
            }
            // refresh API 호출
        }
        // 401시에
        response
    }

    private fun Response.isAccessTokenInvalid(): Boolean = (code == 401)
    private fun Request.putAccessToken(token: String?): Request =
        putHeader(ACCESS_TOKEN_HEADER, ACCESS_TOKEN_FORMAT.format(token))

    private fun Request.putHeader(
        key: String,
        value: String,
    ): Request = newBuilder().addHeader(key, value).build()

    private fun navigateToLogin() {
//        val loginStartIntent = Intent(context, LoginActivity::class.java)
//            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(loginStartIntent)
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AuthInterceptorEntryPoint {
        fun getTokenPreferenceDataSource(): TokenPreferenceDataSource
    }

    companion object {
        private const val ACCESS_TOKEN_HEADER = "Authorization"
        private const val ACCESS_TOKEN_FORMAT = "Bearer %s"
    }
}
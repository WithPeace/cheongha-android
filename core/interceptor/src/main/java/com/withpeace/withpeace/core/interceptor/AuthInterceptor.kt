package com.withpeace.withpeace.core.interceptor

import android.content.Context
import android.util.Log
import com.withpeace.withpeace.core.datastore.dataStore.TokenPreferenceDataSource
import com.withpeace.withpeace.core.network.di.response.TokenResponse
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {

    private val tokenPreferenceDataSource = EntryPointAccessors
        .fromApplication<AuthInterceptorEntryPoint>(context)
        .getTokenPreferenceDataSource()

    private val client = OkHttpClient.Builder().build()

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { tokenPreferenceDataSource.accessToken.firstOrNull() }
        val tokenAddedRequest = chain.request()
            .newBuilder()
            .addHeader(
                ACCESS_TOKEN_HEADER,
                TOKEN_FORMAT.format(accessToken),
            ).build()
        var response = chain.proceed(tokenAddedRequest)

        if (response.code == 401) {
            val refreshToken = runBlocking { tokenPreferenceDataSource.refreshToken.firstOrNull() }

            if (refreshToken == null) {
                navigateToLogin()
            } else {
                runCatching {
                    refreshAccessToken(refreshToken)
                }.onSuccess { tokenResponse ->
                    runBlocking {
                        tokenPreferenceDataSource.updateAccessToken(tokenResponse.accessToken)
                        tokenPreferenceDataSource.updateRefreshToken(tokenResponse.refreshToken)
                    }
                    response = chain.proceed(
                        chain.request().newBuilder().addHeader(
                            ACCESS_TOKEN_HEADER,
                            TOKEN_FORMAT.format(tokenResponse.accessToken),
                        ).build()
                    )
                }.onFailure {
                    navigateToLogin()
                }
            }
        }
        return response
    }

    private fun refreshAccessToken(refreshToken: String): TokenResponse {
        val response: Response = runBlocking {
            withContext(Dispatchers.IO) {
                client.newCall(createAccessTokenRefreshRequest(refreshToken)).execute()
            }
        }
        if (response.isSuccessful) {
            return response.toDto()
        }
        throw IllegalArgumentException()
    }


    private fun createAccessTokenRefreshRequest(refreshToken: String): Request {
        return Request.Builder()
            .url(REFRESH_URL)
            .addHeader(ACCESS_TOKEN_HEADER, TOKEN_FORMAT.format(refreshToken))
            .build()
    }

    private inline fun <reified T> Response.toDto(): T {
        body?.let {
            return Json.decodeFromString<T>(it.string())
        } ?: throw IllegalArgumentException()
    }

    private fun navigateToLogin() {
        Log.e("woogi", "navigateToLogin: 로그인화면으로 이동" )
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AuthInterceptorEntryPoint {
        fun getTokenPreferenceDataSource(): TokenPreferenceDataSource
    }

    companion object {
        private const val REFRESH_URL = "http://49.50.160.170:8080/api/v1/auth/refresh"
        private const val REFRESH_TOKEN_FORMAT = "ReAuthorization"
        private const val ACCESS_TOKEN_HEADER = "Authorization"
        private const val TOKEN_FORMAT = "Bearer %s"
    }
}
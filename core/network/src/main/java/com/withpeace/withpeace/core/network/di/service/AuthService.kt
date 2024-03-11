package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import com.withpeace.withpeace.core.network.di.response.GoogleLoginResponse
import com.withpeace.withpeace.core.network.di.response.TokenResponse
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("/api/v1/auth/google")
    suspend fun googleLogin(
        @Header("Authorization")
        idToken: String,
    ): ApiResponse<BaseResponse<GoogleLoginResponse>>

    @POST("/api/v1/auth/refresh")
    suspend fun refreshAccessToken(): ApiResponse<BaseResponse<TokenResponse>>

    @POST("/api/v1/auth/logout")
    suspend fun logout(): ApiResponse<BaseResponse<String>>
}

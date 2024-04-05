package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import com.withpeace.withpeace.core.network.di.response.LoginResponse
import com.withpeace.withpeace.core.network.di.response.TokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthService {

    @POST("/api/v1/auth/google")
    suspend fun googleLogin(
        @Header("Authorization")
        idToken: String,
    ): ApiResponse<BaseResponse<LoginResponse>>

    @POST("/api/v1/auth/refresh")
    suspend fun refreshAccessToken(
        @Header("Authorization") refreshToken: String,
    ): ApiResponse<BaseResponse<TokenResponse>>

    @POST("/api/v1/auth/logout")
    suspend fun logout(): ApiResponse<BaseResponse<String>>
}

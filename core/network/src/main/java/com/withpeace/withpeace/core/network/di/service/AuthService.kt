package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.request.SignUpRequest
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import com.withpeace.withpeace.core.network.di.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/v1/auth/google")
    fun googleLogin(): ApiResponse<BaseResponse<TokenResponse>>

    @POST("/api/v1/auth/register")
    fun signUp(@Body signUpRequest: SignUpRequest): ApiResponse<BaseResponse<TokenResponse>>

    @POST("/api/v1/auth/refresh")
    fun refreshAccessToken(): ApiResponse<BaseResponse<TokenResponse>>

    @POST("/api/v1/auth/logout")
    fun logout(): ApiResponse<BaseResponse<String>>
}
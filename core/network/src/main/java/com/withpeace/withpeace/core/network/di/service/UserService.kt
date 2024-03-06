package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.request.SignUpRequest
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import com.withpeace.withpeace.core.network.di.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/api/v1/auth/register")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest,
    ): ApiResponse<BaseResponse<TokenResponse>>
}

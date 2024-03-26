package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import com.withpeace.withpeace.core.network.di.response.ProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface UserService {
    @GET("/api/v1/users/profile")
    suspend fun getProfile(): ApiResponse<BaseResponse<ProfileResponse>>

    @PATCH("/api/v1/users/profile/nickname")
    suspend fun updateNickname(nickname: String): ApiResponse<BaseResponse<String>>

    @PATCH("/api/v1/users/profile/image")
    suspend fun updateImage(@Part imageFile: MultipartBody.Part): ApiResponse<BaseResponse<String>>

    @GET("/api/v1/users/profile/nickname/check")
    suspend fun isNicknameDuplicate(@Query("nickname") nickname: String): ApiResponse<BaseResponse<String>>
}

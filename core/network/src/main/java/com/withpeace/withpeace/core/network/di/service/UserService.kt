package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.request.NicknameRequest
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import com.withpeace.withpeace.core.network.di.response.ChangedProfileResponse
import com.withpeace.withpeace.core.network.di.response.ProfileResponse
import com.withpeace.withpeace.core.network.di.response.TokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface UserService {
    @GET("/api/v1/users/profile")
    suspend fun getProfile(): ApiResponse<BaseResponse<ProfileResponse>>

    @PATCH("/api/v1/users/profile/nickname")
    suspend fun updateNickname(@Body nicknameRequest: NicknameRequest): ApiResponse<BaseResponse<String>>

    @Multipart
    @PATCH("/api/v1/users/profile/image")
    suspend fun updateImage(@Part imageFile: MultipartBody.Part): ApiResponse<BaseResponse<String>>

    @Multipart
    @PUT("/api/v1/users/profile")
    suspend fun updateProfile(
        @Part("nickname") nickname: RequestBody,
        @Part imageFile: MultipartBody.Part,
    ): ApiResponse<BaseResponse<ChangedProfileResponse>>

    @GET("/api/v1/users/profile/nickname/check")
    suspend fun isNicknameDuplicate(@Query("nickname") nickname: String): ApiResponse<BaseResponse<Boolean>>

    @POST("/api/v1/auth/logout")
    suspend fun logout(): ApiResponse<BaseResponse<String>>

    @Multipart
    @POST("/api/v1/auth/register")
    suspend fun signUp(
        @Part("nickname") nickname: RequestBody,
        @Part imageFile: MultipartBody.Part,
    ): ApiResponse<BaseResponse<TokenResponse>>

    @Multipart
    @POST("/api/v1/auth/register")
    suspend fun signUp(
        @Part("nickname") nickname: RequestBody,
    ): ApiResponse<BaseResponse<TokenResponse>>

    @DELETE("/api/v1/users")
    suspend fun withdraw(): ApiResponse<BaseResponse<Boolean>>
}

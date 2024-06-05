package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppUpdateService {
    @GET("/api/v1/app/check/android")
    suspend fun shouldUpdate(@Query("currentVersion") currentVersion: Int): ApiResponse<BaseResponse<Boolean>>
}
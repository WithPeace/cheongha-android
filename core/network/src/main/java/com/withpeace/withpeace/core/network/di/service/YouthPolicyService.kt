package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.response.policy.YouthPolicyListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YouthPolicyService {
    @GET("youthPlcyList.do")
    suspend fun getPolicies(
        @Query("openApiVlak") apiKey: String,
        @Query("display") pageSize: Int,
        @Query("pageIndex") pageIndex: Int,
        @Query("bizTycdSel") classification: String?,
        @Query("srchPolyBizSecd") region: String?,
    ): ApiResponse<YouthPolicyListResponse>
}
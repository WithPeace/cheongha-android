package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import com.withpeace.withpeace.core.network.di.response.policy.PolicyDetailResponse
import com.withpeace.withpeace.core.network.di.response.policy.PolicyResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface YouthPolicyService {
    @GET("/api/v1/policies")
    suspend fun getPolicies(
        @Query("region") region: String,
        @Query("classification") classification: String,
        @Query("pageIndex") pageIndex: Int,
        @Query("display") display: Int,
    ): ApiResponse<BaseResponse<List<PolicyResponse>>>

    @GET("/api/v1/policies/{policyId}")
    suspend fun getPolicyDetail(@Path("policyId") policyId: String): ApiResponse<BaseResponse<PolicyDetailResponse>>

    @POST("/api/v1/policies/{policyId}/favorites")
    suspend fun bookmarkPolicy(@Path("policyId") policyId: String): ApiResponse<BaseResponse<Boolean>>

    @DELETE("/api/v1/policies/{policyId}/favorites")
    suspend fun unBookmarkPolicy(@Path("policyId") policyId: String): ApiResponse<BaseResponse<Boolean>>
}
package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.request.CommonCommentRequest
import com.withpeace.withpeace.core.network.di.request.ReportTypeRequest
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentService {
    @POST("/api/v2/comments")
    suspend fun registerComment(
        @Body commentRequest: CommonCommentRequest,
    ): ApiResponse<BaseResponse<Boolean>>

    @POST("/api/v2/comments/{commentId}/report")
    suspend fun reportComment(
        @Path("commentId") commentId: Long,
        @Body reportTypeRequest: ReportTypeRequest,
    ): ApiResponse<BaseResponse<Boolean>>
}
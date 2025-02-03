package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.request.SelectBalanceGameRequest
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import com.withpeace.withpeace.core.network.di.response.GameEntity
import com.withpeace.withpeace.core.network.di.response.SelectBalanceGameResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BalanceGameService {
    @GET("/api/v1/balance-games")
    suspend fun fetchBalanceGame(
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int,
    ): ApiResponse<BaseResponse<List<GameEntity>>>

    @POST("/api/v1/balance-games/{gameId}/choices")
    suspend fun postBalanceGame(
        @Path("gameId") gameId: String,
        @Body choice: SelectBalanceGameRequest,
    ): ApiResponse<BaseResponse<SelectBalanceGameResponse>>
}
package com.withpeace.withpeace.core.data.util

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.network.di.common.getErrorBody

suspend fun <T> ApiResponse<T>.handleApiFailure(
    onError: suspend (ResponseError) -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Failure.Error) {
        val errorBody = errorBody?.getErrorBody()
        onError(
            errorBody?.code?.let { ResponseError.findByCode(it) }
                ?: ResponseError.UNKNOWN_ERROR,
        )
    } else if (this is ApiResponse.Failure.Exception) {
        onError(ResponseError.HTTP_EXCEPTION_ERROR)
    }
    return this
}
package com.withpeace.withpeace.core.domain.model

sealed interface Response<T> {
    data class Success<T>(val data: T) : Response<T>
    data class Failure<T>(
        val errorCode: Int? = null,
        val errorMessage: String? = null,
    ) : Response<T>
}

package com.withpeace.withpeace.core.domain.model

sealed interface Response<T> {
    data class Success<T>(val data: T) : Response<T>

    data class Failure<T>(
        val errorCode: Int? = null,
        val errorMessage: String? = null,
    ) : Response<T>

}

inline fun <T> Response<T>.onSuccess(action: (value: T) -> Unit): Response<T> {
    if (this is Response.Success) action(data)
    return this
}

inline fun <T> Response<T>.onFailure(action: (errorCode: Int?, errorMessage: String?) -> Unit): Response<T> {
    if (this is Response.Failure) action(errorCode, errorMessage)
    return this
}

fun test() {
    val response: Response<Boolean> = Response.Success(true)
    response.onSuccess {

    }.onFailure { errorCode, errorMessage ->

    }
}


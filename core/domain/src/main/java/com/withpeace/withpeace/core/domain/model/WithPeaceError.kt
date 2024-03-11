package com.withpeace.withpeace.core.domain.model
sealed interface WithPeaceError {
    data class GeneralError(val code: Int?, val message: String?): WithPeaceError
    data class UnAuthorized(val code: Int?, val message: String?) : WithPeaceError
}
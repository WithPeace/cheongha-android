package com.withpeace.withpeace.core.domain.model
sealed interface WithPeaceError {
    data class GeneralError(val code: Int? = null, val message: String? = null) : WithPeaceError
    data class UnAuthorized(val code: Int? = null, val message: String? = null) : WithPeaceError
}
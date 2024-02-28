package com.withpeace.withpeace.feature.login

sealed interface LoginUiEvent {

    data object LoginSuccess: LoginUiEvent

    data class LoginFail(val message: String): LoginUiEvent
}
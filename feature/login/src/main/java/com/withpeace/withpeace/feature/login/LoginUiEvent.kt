package com.withpeace.withpeace.feature.login

sealed interface LoginUiEvent {

    data object SignUpSuccess: LoginUiEvent

    data class SignUpFail(val message: String): LoginUiEvent
}
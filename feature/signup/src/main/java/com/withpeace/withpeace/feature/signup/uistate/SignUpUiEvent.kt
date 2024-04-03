package com.withpeace.withpeace.feature.signup.uistate

sealed interface SignUpUiEvent {
    data object SignUpSuccess : SignUpUiEvent
    data object SignUpFail : SignUpUiEvent
    data object VerifyFail : SignUpUiEvent
    data object UnAuthorized : SignUpUiEvent
    data object NicknameInValid : SignUpUiEvent
}
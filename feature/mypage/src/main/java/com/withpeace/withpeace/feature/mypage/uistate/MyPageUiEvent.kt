package com.withpeace.withpeace.feature.mypage.uistate

sealed interface MyPageUiEvent {
    data object UnAuthorizedError: MyPageUiEvent
    data object GeneralError: MyPageUiEvent
}
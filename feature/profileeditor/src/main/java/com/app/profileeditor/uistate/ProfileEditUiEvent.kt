package com.app.profileeditor.uistate

sealed interface ProfileEditUiEvent {
    data object NicknameDuplicated : ProfileEditUiEvent
    data object NicknameInvalidFormat : ProfileEditUiEvent
    data class UpdateSuccess(val nickname: String, val imageUrl: String) : ProfileEditUiEvent
    data object UpdateFailure : ProfileEditUiEvent
    data object ProfileUnchanged : ProfileEditUiEvent
    data object UnAuthorized : ProfileEditUiEvent
}
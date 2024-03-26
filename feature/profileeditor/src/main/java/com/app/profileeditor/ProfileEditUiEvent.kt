package com.app.profileeditor

sealed interface ProfileEditUiEvent {
    data object ShowDuplicate : ProfileEditUiEvent
    data object ShowNicknameVerified : ProfileEditUiEvent
    data object ShowInvalidFormat : ProfileEditUiEvent
    data object ShowUpdateSuccess : ProfileEditUiEvent
    data object ShowFailure : ProfileEditUiEvent
    data object ShowUnchanged : ProfileEditUiEvent
}
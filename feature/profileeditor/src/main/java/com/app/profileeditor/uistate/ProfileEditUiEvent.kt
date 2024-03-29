package com.app.profileeditor.uistate

sealed interface ProfileEditUiEvent {
    data object ShowDuplicate : ProfileEditUiEvent
    data object ShowDuplicateSnackBar : ProfileEditUiEvent
    data object ShowNicknameVerified : ProfileEditUiEvent
    data object ShowInvalidFormat : ProfileEditUiEvent
    data object ShowUpdateSuccess : ProfileEditUiEvent
    data object ShowFailure : ProfileEditUiEvent
    data object ShowUnchanged : ProfileEditUiEvent
}
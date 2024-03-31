package com.app.profileeditor.uistate

sealed interface ProfileEditUiEvent {
    data object ShowDuplicateSnackBar : ProfileEditUiEvent
    data object ShowInvalidFormatSnackBar : ProfileEditUiEvent
    data object ShowUpdateSuccess : ProfileEditUiEvent
    data object ShowFailure : ProfileEditUiEvent
    data object ShowUnchanged : ProfileEditUiEvent
    data object UnAuthorized : ProfileEditUiEvent
}
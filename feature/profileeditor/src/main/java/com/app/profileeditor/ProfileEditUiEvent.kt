package com.app.profileeditor

sealed interface ProfileEditUiEvent {
    data object ShowDuplicate : ProfileEditUiEvent
    data object ShowInvalidFormat : ProfileEditUiEvent
    data object ShowSuccess : ProfileEditUiEvent
    data object ShowFailure : ProfileEditUiEvent
    data object ShowUnchanged : ProfileEditUiEvent
}
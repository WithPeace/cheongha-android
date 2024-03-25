package com.app.profileeditor

sealed interface ProfileEditUiState {
    data class Editing(
        val nickname: String,
        val profileImage: String,
        val isBasicTextValid: Boolean
    ) : ProfileEditUiState

    data object NoChanges : ProfileEditUiState
}
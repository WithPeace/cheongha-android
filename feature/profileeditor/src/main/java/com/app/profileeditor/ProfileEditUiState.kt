package com.app.profileeditor

sealed interface ProfileEditUiState {
    data class Editing(
        val nickname: String,
        val profileImage: String,
    ) : ProfileEditUiState

    data object NoChanges : ProfileEditUiState
}
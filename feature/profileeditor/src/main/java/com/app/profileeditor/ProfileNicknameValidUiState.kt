package com.app.profileeditor

sealed interface ProfileNicknameValidUiState {
    data object Valid : ProfileNicknameValidUiState
    data object InValidFormat : ProfileNicknameValidUiState
    data object InValidDuplicated : ProfileNicknameValidUiState
}
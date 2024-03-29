package com.app.profileeditor.uistate

sealed interface ProfileNicknameValidUiState {
    data object Valid : ProfileNicknameValidUiState
    data object InValidFormat : ProfileNicknameValidUiState
    data object InValidDuplicated : ProfileNicknameValidUiState
}
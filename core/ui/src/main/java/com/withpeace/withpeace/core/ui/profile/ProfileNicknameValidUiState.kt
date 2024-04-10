package com.withpeace.withpeace.core.ui.profile

sealed interface ProfileNicknameValidUiState {
    data object Valid : ProfileNicknameValidUiState
    data object InValidFormat : ProfileNicknameValidUiState
    data object InValidDuplicated : ProfileNicknameValidUiState
    data object Changing : ProfileNicknameValidUiState
}
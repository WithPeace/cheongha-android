package com.withpeace.withpeace.feature.mypage.uistate

sealed interface MyPageUiState {
    data object Loading : MyPageUiState
    data class Success(val profileInfo: ProfileInfoUiModel) : MyPageUiState
}
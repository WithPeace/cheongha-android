package com.withpeace.withpeace.feature.mypage.uistate

sealed interface ProfileUiState {
    data class Success(val profileInfoUiModel: ProfileInfoUiModel) : ProfileUiState
    data object Loading : ProfileUiState
    data object Failure : ProfileUiState
}
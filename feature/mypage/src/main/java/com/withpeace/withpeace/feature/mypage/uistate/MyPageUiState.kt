package com.withpeace.withpeace.feature.mypage.uistate

import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo

sealed interface MyPageUiState {
    data object Loading : MyPageUiState
    data class Success(val profileInfo: ProfileInfoUiModel) : MyPageUiState
    data object Fail : MyPageUiState
}
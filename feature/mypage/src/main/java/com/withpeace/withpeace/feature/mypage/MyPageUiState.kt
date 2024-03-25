package com.withpeace.withpeace.feature.mypage

import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo

sealed interface MyPageUiState {
    data object Loading : MyPageUiState
    data class Success(val profileInfo: ProfileInfo) : MyPageUiState
    data object Fail : MyPageUiState
}
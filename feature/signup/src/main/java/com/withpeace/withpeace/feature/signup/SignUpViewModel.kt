package com.withpeace.withpeace.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.VerifyNicknameUseCase
import com.withpeace.withpeace.core.ui.profile.ProfileNicknameValidUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val verifyNicknameUseCase: VerifyNicknameUseCase,
) : ViewModel() {
    private val _signUpInfo = MutableStateFlow(
        SignUpUiModel(
            "", "default.png",
        ),
    )
    val signUpUiModel = _signUpInfo.asStateFlow()

    private val _profileNicknameValidUiState =
        MutableStateFlow<ProfileNicknameValidUiState>(ProfileNicknameValidUiState.Valid)
    val profileNicknameValidUiState = _profileNicknameValidUiState.asStateFlow()

    fun signUp() {
    }

    fun onNickNameChanged(nickname: String) {
        _signUpInfo.update { it.copy(nickname = nickname) }
    }

    fun onImageChanged(imageUri: String) {
        _signUpInfo.update { it.copy(profileImage = imageUri) }
    }

    fun verifyNickname() {
        viewModelScope.launch {
            if(_signUpInfo.value.nickname.isEmpty()) {

                return@launch
            }
            verifyNicknameUseCase(
                nickname = _signUpInfo.value.nickname,
                onError = {
                },
            )
        }
    }
}
package com.withpeace.withpeace.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.usecase.SignUpUseCase
import com.withpeace.withpeace.core.domain.usecase.VerifyNicknameUseCase
import com.withpeace.withpeace.core.ui.profile.ProfileNicknameValidUiState
import com.withpeace.withpeace.feature.signup.uistate.SignUpUiEvent
import com.withpeace.withpeace.feature.signup.uistate.SignUpUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val verifyNicknameUseCase: VerifyNicknameUseCase,
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {
    private val _signUpInfo = MutableStateFlow(
        SignUpUiModel(
            "", null,
        ),
    )
    val signUpUiModel = _signUpInfo.asStateFlow()

    private val _profileNicknameValidUiState =
        MutableStateFlow<ProfileNicknameValidUiState>(ProfileNicknameValidUiState.Valid)
    val profileNicknameValidUiState = _profileNicknameValidUiState.asStateFlow()

    private val _signUpEvent = Channel<SignUpUiEvent>()
    val signUpEvent = _signUpEvent.receiveAsFlow()

    fun onNickNameChanged(nickname: String) {
        _signUpInfo.update { it.copy(nickname = nickname) }
    }

    fun verifyNickname() {
        viewModelScope.launch {
            if (_signUpInfo.value.nickname.isEmpty()) {
                _profileNicknameValidUiState.update { ProfileNicknameValidUiState.Valid }
                return@launch
            }
            verifyNicknameUseCase(
                nickname = _signUpInfo.value.nickname,
                onError = { error ->
                    when (error) {
                        is WithPeaceError.GeneralError -> {
                            when (error.code) {
                                1 -> _profileNicknameValidUiState.update { ProfileNicknameValidUiState.InValidFormat }
                                2 -> _profileNicknameValidUiState.update { ProfileNicknameValidUiState.InValidDuplicated }
                            }
                        }

                        else -> _signUpEvent.send(SignUpUiEvent.VerifyFail)
                    }
                },
            ).collect {
                _profileNicknameValidUiState.update { ProfileNicknameValidUiState.Valid }
            }
        }
    }

    fun onImageChanged(imageUri: String?) {
        _signUpInfo.update { it.copy(profileImage = imageUri) }
    }

    fun signUp() {
        viewModelScope.launch {
            if (_signUpInfo.value.nickname.isEmpty()) {
                _signUpEvent.send(SignUpUiEvent.EmptyNickname)
                return@launch
            }
            viewModelScope.launch {
                signUpUseCase(
                    signUpUiModel.value.toDomain(),
                    onError = {
                        when (it) {
                            is WithPeaceError.GeneralError -> {
                                when (it.code) {
                                    40001 -> SignUpUiEvent.NicknameInvalidFormat
                                    40007 -> SignUpUiEvent.NicknameDuplicated
                                    else -> SignUpUiEvent.SignUpFail
                                }
                            }

                            is WithPeaceError.UnAuthorized -> {
                                _signUpEvent.send(SignUpUiEvent.UnAuthorized)
                            }
                        }
                    },
                ).collect {
                    _signUpEvent.send(SignUpUiEvent.SignUpSuccess)
                }
            }
        }
    }
}

// 테스트 히스토리
// 아무것도 입력 안했을 떄
// 중복된 닉네임
// 닉네임과 이미지 함께 전송

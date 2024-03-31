package com.app.profileeditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.profileeditor.navigation.PROFILE_IMAGE_URL_ARGUMENT
import com.app.profileeditor.navigation.PROFILE_NICKNAME_ARGUMENT
import com.app.profileeditor.uistate.ProfileEditUiEvent
import com.app.profileeditor.uistate.ProfileEditUiState
import com.app.profileeditor.uistate.ProfileNicknameValidUiState
import com.app.profileeditor.uistate.ProfileUiModel
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.usecase.UpdateProfileUseCase
import com.withpeace.withpeace.core.domain.usecase.VerifyNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val verifyNicknameUseCase: VerifyNicknameUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {
    val baseProfileInfo = ProfileUiModel(
        nickname = savedStateHandle.get<String>(PROFILE_NICKNAME_ARGUMENT) ?: "",
        profileImage = savedStateHandle.get<String>(PROFILE_IMAGE_URL_ARGUMENT) ?: "default.png",
    ) // 최초 정보에서 변경사항이 있는지 비교를 위한 필드

    private val _profileEditUiState =
        MutableStateFlow<ProfileEditUiState>(
            ProfileEditUiState(baseProfileInfo, false),
        )
    val profileEditUiState = _profileEditUiState.asStateFlow()

    private val _profileEditUiEvent = Channel<ProfileEditUiEvent>()
    val profileEditUiEvent = _profileEditUiEvent.receiveAsFlow()

    private val _profileNicknameValidUiState =
        MutableStateFlow<ProfileNicknameValidUiState>(ProfileNicknameValidUiState.Valid)
    val profileNicknameValidUiState = _profileNicknameValidUiState.asStateFlow()

    fun onImageChanged(imageUri: String) {
        val changingUiState = _profileEditUiState.value.currentProfileInfo.copy(
            nickname = _profileEditUiState.value.currentProfileInfo.nickname,
            profileImage = imageUri,
        )
        _profileEditUiState.update {
            return@update if (baseProfileInfo == changingUiState
            ) {
                _profileEditUiState.value.copy(
                    currentProfileInfo = baseProfileInfo,
                    isChanged = false,
                )
            } else {
                _profileEditUiState.value.copy(
                    currentProfileInfo = changingUiState,
                    isChanged = true,
                )
            }
        }
    }
    fun onNickNameChanged(nickname: String) {
        val changingUiState = _profileEditUiState.value.currentProfileInfo.copy(
            nickname = nickname,
            profileImage = _profileEditUiState.value.currentProfileInfo.profileImage,
        )
        _profileEditUiState.update {
            // changingUiState.toDomain().getChangingState(baseProfileInfo.toDomain()).toUiModel()
            return@update if (baseProfileInfo == changingUiState
            ) {
                _profileEditUiState.value.copy(
                    currentProfileInfo = baseProfileInfo,
                    isChanged = false,
                )
            } else {
                _profileEditUiState.value.copy(
                    currentProfileInfo = changingUiState,
                    isChanged = true,
                )
            }
        }
    }

    fun verifyNickname() {
        if (profileEditUiState.value.isChanged.not()) {
            return
        }
        viewModelScope.launch {
            verifyNicknameUseCase(
                onError = { error ->
                        _profileEditUiEvent.send(
                            when (error) {
                                is WithPeaceError.GeneralError -> {
                                    when (error.code) {
                                        1 -> ProfileEditUiEvent.ShowInvalidFormat
                                        2 -> ProfileEditUiEvent.ShowDuplicate
                                        else -> ProfileEditUiEvent.ShowFailure
                                    }
                                }

                                else -> ProfileEditUiEvent.ShowFailure
                            },
                        )
                },
                nickname = _profileEditUiState.value.currentProfileInfo.nickname,
            ).collect {
                _profileEditUiEvent.send(
                    ProfileEditUiEvent.ShowNicknameVerified,
                )
            }
        }
    }

    fun updateIsNicknameValidStatus(status: ProfileNicknameValidUiState) {
        _profileNicknameValidUiState.update { status }
    }

    fun updateProfile() {
        viewModelScope.launch {
            if (_profileEditUiState.value.isChanged.not()) {
                _profileEditUiEvent.send(ProfileEditUiEvent.ShowUnchanged)
            } else if (_profileEditUiState.value.isChanged) {
                updateProfileUseCase(
                    beforeProfile = baseProfileInfo.toDomain(),
                    afterProfile = _profileEditUiState.value.currentProfileInfo.toDomain(),
                    onError = {
                        _profileEditUiEvent.send(ProfileEditUiEvent.ShowFailure)

                    },
                ).collect {
                    _profileEditUiEvent.send(ProfileEditUiEvent.ShowUpdateSuccess)
                }
            }
        }
    }
}

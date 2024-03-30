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
            ProfileEditUiState.NoChanges,
        )
    val profileEditUiState = _profileEditUiState.asStateFlow()

    private val _profileEditUiEvent = Channel<ProfileEditUiEvent>()
    val profileEditUiEvent = _profileEditUiEvent.receiveAsFlow()

    private val _profileNicknameValidUiState =
        MutableStateFlow<ProfileNicknameValidUiState>(ProfileNicknameValidUiState.Valid)
    val profileNicknameValidUiState = _profileNicknameValidUiState.asStateFlow()

    fun onImageChanged(imageUri: String) {
        _profileEditUiState.update {
            val updateData = ProfileEditUiState.Editing(
                ProfileUiModel(
                    (it as? ProfileEditUiState.Editing)?.profileInfo?.nickname
                        ?: baseProfileInfo.nickname,
                    profileImage = imageUri,
                ),
            )
            if (baseProfileInfo == updateData.profileInfo) {
                return@update ProfileEditUiState.NoChanges
            }
            updateData
        }
    }

    fun onNickNameChanged(nickname: String) {
        _profileEditUiState.update {
            val updateData = ProfileEditUiState.Editing(
                ProfileUiModel(
                    nickname = nickname,
                    profileImage = (it as? ProfileEditUiState.Editing)?.profileInfo?.profileImage
                        ?: baseProfileInfo.profileImage,
                ),
            ) // Editing 중이면 값을 갱신, 아닐 경우 기본 값에 nickname만 값을 추가
            if (baseProfileInfo == updateData.profileInfo) {
                return@update ProfileEditUiState.NoChanges
            } // 변경 값이 기본 값일 경우 noChanges 상태
            updateData
        }
    }

    fun verifyNickname() {
        if (profileEditUiState.value is ProfileEditUiState.NoChanges) {
            return
        }
        viewModelScope.launch {
            verifyNicknameUseCase(
                onError = { error ->
                    this.launch {
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
                    }
                },
                nickname = (profileEditUiState.value as ProfileEditUiState.Editing).profileInfo.nickname,
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
            if (_profileEditUiState.value is ProfileEditUiState.NoChanges) {
                _profileEditUiEvent.send(ProfileEditUiEvent.ShowUnchanged)
            } else if (_profileEditUiState.value is ProfileEditUiState.Editing) {
                val editing = _profileEditUiState.value as ProfileEditUiState.Editing
                updateProfileUseCase(
                    beforeProfile = baseProfileInfo.toDomain(),
                    afterProfile = editing.profileInfo.toDomain(),
                    onError = {
                        viewModelScope.launch {
                            _profileEditUiEvent.send(ProfileEditUiEvent.ShowFailure)
                        }
                    },
                ).collect {
                    _profileEditUiEvent.send(ProfileEditUiEvent.ShowUpdateSuccess)
                }
            }
        }
    }
}

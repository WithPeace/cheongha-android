package com.app.profileeditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.app.profileeditor.navigation.PROFILE_IMAGE_URL_ARGUMENT
import com.app.profileeditor.navigation.PROFILE_NICKNAME_ARGUMENT
import com.withpeace.withpeace.core.domain.model.profile.ChangingProfileInfo
import com.withpeace.withpeace.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
) : ViewModel() {
    val baseProfileInfo = ChangingProfileInfo(
        nickname = savedStateHandle.get<String>(PROFILE_NICKNAME_ARGUMENT) ?: "",
        profileImage = savedStateHandle.get<String>(PROFILE_IMAGE_URL_ARGUMENT) ?: "default.png",
    ) // 최초 정보에서 변경사항이 있는지 비교를 위한 필드

    private val _profileEditUiState =
        MutableStateFlow<ProfileEditUiState>(
            ProfileEditUiState.NoChanges,
        )
    val profileEditUiState = _profileEditUiState.asStateFlow()

    fun onImageChanged(imageUri: String) {
        _profileEditUiState.update {
            val updateData = ProfileEditUiState.Editing(
                (it as? ProfileEditUiState.Editing)?.nickname
                    ?: baseProfileInfo.nickname,
                profileImage = imageUri,
                isBasicTextValid = false,
            )
            if (baseProfileInfo.isSameTo(updateData.nickname, updateData.profileImage)) {
                return@update ProfileEditUiState.NoChanges
            }
            updateData
        }
    }

    fun onNickNameChanged(nickname: String) {
        _profileEditUiState.update {
            val updateData = ProfileEditUiState.Editing(
                nickname = nickname,
                profileImage = (it as? ProfileEditUiState.Editing)?.profileImage
                    ?: baseProfileInfo.profileImage ?: "default.png",
                isBasicTextValid = false,
            )
            if (baseProfileInfo.isSameTo(updateData.nickname, updateData.profileImage)) {
                return@update ProfileEditUiState.NoChanges
            }
            updateData
        }
    }

    fun updateProfile() {
        if (_profileEditUiState.value is ProfileEditUiState.NoChanges) {

        }
    }
}
// 1. 다이어로그
// 2. 하단 문구 및 오류 표시
// 3. updateProfileUsecase() 3개 있어야겠다. ㅋㅋ
// 4. 오류 표시
// 5, 이전 화면 갱신
package com.app.profileeditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.app.profileeditor.navigation.PROFILE_IMAGE_URL_ARGUMENT
import com.app.profileeditor.navigation.PROFILE_NICKNAME_ARGUMENT
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
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
    // 변경이 되었는지 알 수 있어야한다.
    // validation status
    private val _profileInfo = MutableStateFlow(
        ProfileInfo(
            nickname = savedStateHandle.get<String>(PROFILE_NICKNAME_ARGUMENT) ?: "",
            profileImageUrl = savedStateHandle.get<String>(PROFILE_IMAGE_URL_ARGUMENT) ?: "",
            "",
        ),
    )
    val profileInfo = _profileInfo.asStateFlow()

    fun onImageUriAdded(imageUri: String) {
        _profileInfo.update { it.copy(profileImageUrl = imageUri) }
    }

    fun onNickNameChanged(nickname: String) {
        _profileInfo.update { it.copy(nickname = nickname) }
    }
}
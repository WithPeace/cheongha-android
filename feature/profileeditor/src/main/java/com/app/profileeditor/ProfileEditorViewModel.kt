package com.app.profileeditor

import androidx.lifecycle.ViewModel
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileEditorViewModel @Inject constructor() : ViewModel() {
    // 변경이 되었는지 알 수 있어야한다.
    // validation status
    private val _profileInfo = MutableStateFlow(
        ProfileInfo(
            nickname = "",
            profileImageUrl = null,
            ""
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
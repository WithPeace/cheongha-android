package com.withpeace.withpeace.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.usecase.GetProfileInfoUseCase
import com.withpeace.withpeace.core.domain.usecase.LogoutUseCase
import com.withpeace.withpeace.feature.mypage.uistate.MyPageUiEvent
import com.withpeace.withpeace.feature.mypage.uistate.ProfileInfoUiModel
import com.withpeace.withpeace.feature.mypage.uistate.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetProfileInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _myPageUiEvent = Channel<MyPageUiEvent>()
    val myPageUiEvent = _myPageUiEvent.receiveAsFlow()

    private val _profileUiModel = MutableStateFlow(
        ProfileInfoUiModel(
            "nickname",
            "default.png",
            "",
        ),
    )
    val myPageUiState = _profileUiModel.asStateFlow()

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            getUserInfoUseCase { error ->
                when (error) {
                    ResponseError.EXPIRED_TOKEN_ERROR -> {
                       logout()
                    }
                    else -> {
                        _myPageUiEvent.send(MyPageUiEvent.ResponseError)
                    }
                }
            }.collect { profileInfo ->
                _profileUiModel.update {
                    profileInfo.toUiModel()
                }
            }
        }
    }

    fun updateProfile(nickname: String?, profileUrl: String?) {
        _profileUiModel.update {
            it.copy(
                nickname = nickname ?: it.nickname,
                profileImage = profileUrl ?: it.profileImage,
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase {
                _myPageUiEvent.send(
                    MyPageUiEvent.ResponseError
                )
            }.collect {
                _myPageUiEvent.send(MyPageUiEvent.Logout)
            }
        }
    }
}
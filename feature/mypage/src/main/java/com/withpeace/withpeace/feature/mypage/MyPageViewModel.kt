package com.withpeace.withpeace.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.usecase.GetProfileInfoUseCase
import com.withpeace.withpeace.core.domain.usecase.LogoutUseCase
import com.withpeace.withpeace.core.domain.usecase.WithdrawUseCase
import com.withpeace.withpeace.feature.mypage.uistate.MyPageUiEvent
import com.withpeace.withpeace.feature.mypage.uistate.ProfileInfoUiModel
import com.withpeace.withpeace.feature.mypage.uistate.ProfileUiState
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
    private val withdrawUseCase: WithdrawUseCase,
) : ViewModel() {
    private val _myPageUiEvent = Channel<MyPageUiEvent>()
    val myPageUiEvent = _myPageUiEvent.receiveAsFlow()

    private val _profileUiState = MutableStateFlow<ProfileUiState>(
        ProfileUiState.Loading,
    )
    val profileUiState = _profileUiState.asStateFlow()

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            getUserInfoUseCase { error ->
                when (error) {
                    ResponseError.EXPIRED_TOKEN_ERROR -> {
                        _myPageUiEvent.send(MyPageUiEvent.UnAuthorizedError)
                    }
                    else -> {
                        _myPageUiEvent.send(MyPageUiEvent.ResponseError)
                    }
                }
                _profileUiState.update { ProfileUiState.Failure }
            }.collect { profileInfo ->
                _profileUiState.update {
                    ProfileUiState.Success(profileInfo.toUiModel())
                }
            }
        }
    }

    fun updateProfile(nickname: String?, profileUrl: String?) {
        if (nickname == null && profileUrl == null) {
            return
        }
        _profileUiState.update {
            val profileInfo = (it as ProfileUiState.Success).profileInfoUiModel
            ProfileUiState.Success(
                ProfileInfoUiModel(
                    nickname = nickname ?: profileInfo.nickname,
                    profileImage = profileUrl ?: profileInfo.profileImage,
                    email = profileInfo.email,
                ),
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase {
                _myPageUiEvent.send(
                    MyPageUiEvent.ResponseError,
                )
            }.collect {
                _myPageUiEvent.send(MyPageUiEvent.Logout)
            }
        }
    }

    fun withdraw() {
        viewModelScope.launch {
            withdrawUseCase {
                when (it) {
                    ResponseError.EXPIRED_TOKEN_ERROR -> {
                        _myPageUiEvent.send(MyPageUiEvent.UnAuthorizedError)
                    }

                    else -> {
                        _myPageUiEvent.send(MyPageUiEvent.ResponseError)
                    }
                }
            }.collect {
                _myPageUiEvent.send(MyPageUiEvent.WithdrawSuccess)
            }

        }
    }
}
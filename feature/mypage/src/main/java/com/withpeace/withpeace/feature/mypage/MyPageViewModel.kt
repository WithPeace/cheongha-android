package com.withpeace.withpeace.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.feature.mypage.uistate.MyPageUiState
import com.withpeace.withpeace.feature.mypage.uistate.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _myPageUiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
    val myPageUiState = _myPageUiState.asStateFlow()

    fun getProfile() {
        viewModelScope.launch {
            userRepository.getProfile { error ->
            }.collect { profileInfo ->
                _myPageUiState.update {
                    MyPageUiState.Success(
                        profileInfo.toUiModel(),
                    )
                }
            }
        }
    }
}
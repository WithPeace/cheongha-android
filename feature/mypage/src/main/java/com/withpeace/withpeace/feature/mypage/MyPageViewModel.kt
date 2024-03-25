package com.withpeace.withpeace.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import com.withpeace.withpeace.core.domain.repository.UserRepository
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
    val postUiState = _myPageUiState.asStateFlow()

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            userRepository.getProfile { error ->
            }.collect { profileInfo ->
                _myPageUiState.update {
                    MyPageUiState.Success(
                        profileInfo,
                    )
                }
            }
        }

    }
}
package com.withpeace.withpeace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.CheckAppUpdateUseCase
import com.withpeace.withpeace.core.domain.usecase.IsLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isLoginUseCase: IsLoginUseCase,
    private val checkAppUpdateUseCase: CheckAppUpdateUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init  {
        checkUpdate()
    }

    private fun checkUpdate() {
        viewModelScope.launch {
            checkAppUpdateUseCase(
                currentVersion = BuildConfig.VERSION_CODE,
                onError = {
                    _uiState.update { MainUiState.Error }
                },
            ).collect { shouldUpdate ->
                if (shouldUpdate) {
                    _uiState.update {MainUiState.Update  }
                    return@collect
                }
                val isLogin = isLoginUseCase()
                if (isLogin) {
                    _uiState.update { MainUiState.Home }
                } else {
                    _uiState.update { MainUiState.Login }
                }
            }
        }
    }
}


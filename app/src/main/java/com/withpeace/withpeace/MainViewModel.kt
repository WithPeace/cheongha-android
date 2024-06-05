package com.withpeace.withpeace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.CheckAppUpdateUseCase
import com.withpeace.withpeace.core.domain.usecase.IsLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isLoginUseCase: IsLoginUseCase,
    private val checkAppUpdateUseCase: CheckAppUpdateUseCase,
) : ViewModel() {
    private val _uiState: Channel<MainUiState> = Channel()
    val uiState = _uiState.receiveAsFlow()

    init  {
        checkUpdate()
    }

    private fun checkUpdate() {
        viewModelScope.launch {
            checkAppUpdateUseCase(
                currentVersion = BuildConfig.VERSION_CODE,
                onError = {
                    _uiState.send(MainUiState.Error)
                },
            ).collect { shouldUpdate ->
                if (shouldUpdate) {
                    _uiState.send(MainUiState.Update)
                    return@collect
                }
                val isLogin = isLoginUseCase()
                if (isLogin) {
                    _uiState.send(MainUiState.Home)
                } else {
                    _uiState.send(MainUiState.Login)
                }
            }
        }
    }
}


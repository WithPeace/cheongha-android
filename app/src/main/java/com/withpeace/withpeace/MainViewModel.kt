package com.withpeace.withpeace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.IsLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isLoginUseCase: IsLoginUseCase,
) : ViewModel() {
    private val _isLogin: Channel<Boolean> = Channel()
    val isLogin = _isLogin.receiveAsFlow()

    init {
        viewModelScope.launch { _isLogin.send(isLoginUseCase()) }
    }
}

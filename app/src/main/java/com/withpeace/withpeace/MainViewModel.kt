package com.withpeace.withpeace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.IsLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isLoginUseCase: IsLoginUseCase,
) : ViewModel() {
    private val _isLogin: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isLogin = _isLogin.asSharedFlow()

    init {
        viewModelScope.launch { _isLogin.emit(isLoginUseCase()) }
    }
}

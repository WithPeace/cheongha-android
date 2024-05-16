package com.withpeace.withpeace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.IsLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isLoginUseCase: IsLoginUseCase,
) : ViewModel() {
    private val _isLogin: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isLogin = _isLogin.asStateFlow()

    init  {
        viewModelScope.launch { _isLogin.value = isLoginUseCase() }
    }
}

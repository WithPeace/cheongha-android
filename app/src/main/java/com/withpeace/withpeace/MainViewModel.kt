package com.withpeace.withpeace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel() {
    private val _isLogin: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isLogin = _isLogin.asSharedFlow()

    init {
        viewModelScope.launch {
            val token = tokenRepository.getAccessToken().firstOrNull()
            if(token==null) {
                _isLogin.emit(false)
            } else {
                _isLogin.emit(true)
            }
        }
    }
}
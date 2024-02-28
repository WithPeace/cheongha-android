package com.withpeace.withpeace.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
) : ViewModel() {

    private val _loginUiEvent: MutableSharedFlow<LoginUiEvent> = MutableSharedFlow()
    val loginUiEvent = _loginUiEvent.asSharedFlow()

    fun googleLogin(idToken: String) {
        viewModelScope.launch {
            tokenRepository.googleLogin(idToken) {
                Log.e("woogi", it ?: "메시지 없음")
                launch {
                    _loginUiEvent.emit(LoginUiEvent.LoginFail(it ?: "메시지 없음"))
                }
            }.collect { token ->
                tokenRepository.updateAccessToken(token.accessToken)
                tokenRepository.updateRefreshToken(token.refreshToken)
                _loginUiEvent.emit(LoginUiEvent.LoginSuccess)
                signUp()
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            tokenRepository.signUp(
                email = "wooseok",
                nickname = "haha",
                deviceToken = null,
            )
        }
    }
}

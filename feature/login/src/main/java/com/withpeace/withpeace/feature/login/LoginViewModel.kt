package com.withpeace.withpeace.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.Response
import com.withpeace.withpeace.core.domain.usecase.GoogleLoginUseCase
import com.withpeace.withpeace.core.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {

    private val _loginUiEvent: Channel<LoginUiEvent> = Channel()
    val loginUiEvent = _loginUiEvent.receiveAsFlow()

    fun googleLogin(idToken: String) {
        viewModelScope.launch {
            googleLoginUseCase(
                idToken = idToken,
            ).collect { response ->
                when (response) {
                    is Response.Failure -> _loginUiEvent.send(LoginUiEvent.LoginFail)
                    is Response.Success -> {
                        _loginUiEvent.send(LoginUiEvent.LoginSuccess)
                        signUp("abc", "abc")
                    }
                }
            }
        }
    }

    fun signUp(
        email: String,
        nickname: String,
    ) {
        viewModelScope.launch {
            signUpUseCase(
                email = email,
                nickname = nickname,
            ).collect { response ->
                when (response) {
                    is Response.Failure ->
                        _loginUiEvent.send(LoginUiEvent.SignUpFail(response.errorMessage))

                    is Response.Success -> _loginUiEvent.send(LoginUiEvent.SignUpSuccess)
                }
            }
        }
    }
}

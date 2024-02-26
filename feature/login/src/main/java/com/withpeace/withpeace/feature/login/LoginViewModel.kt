package com.withpeace.withpeace.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
): ViewModel() {

    fun googleLogin(idToken: String){
        viewModelScope.launch {
            tokenRepository.googleLogin(idToken){
                Log.e("woogi", "googleLogin: 로그인 안됨", )
            }.collect{
                Log.d("woogi", "googleLogin: $it")
            }
        }
    }
}
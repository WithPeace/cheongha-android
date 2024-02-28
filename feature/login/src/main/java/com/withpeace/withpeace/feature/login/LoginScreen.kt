package com.withpeace.withpeace.feature.login

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.withpeace.withpeace.googlelogin.GoogleLoginManager
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {
    val viewModel: LoginViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val googleLoginManager = GoogleLoginManager(LocalContext.current)
    Row(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = {
                googleLoginManager.startLogin(
                    coroutineScope,
                    onSuccessLogin = {
                        Log.d("woogi", "idToken: $it")
                        viewModel.googleLogin(it)
                    },
                    onFailLogin = { Log.d("wooseok", it.toString()) },
                )
            },
        ) {
            Text(text = "Login")
        }
        Button(
            onClick = {
                viewModel.signUp()
            },
        ) {
            Text(text = "signup")
        }
    }
}

@Preview(widthDp = 400, heightDp = 900)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}

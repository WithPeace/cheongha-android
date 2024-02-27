package com.withpeace.withpeace.feature.login

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.googlelogin.GoogleLoginManager

@Composable
fun LoginScreen() {
    val coroutineScope = rememberCoroutineScope()
    val googleLoginManager = GoogleLoginManager(LocalContext.current)
    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            Button(
                onClick = {
                    googleLoginManager.startLogin(
                        coroutineScope,
                        onSuccessLogin = { Log.d("Wooseok", it) },
                        onFailLogin = { Log.d("wooseok", it.toString()) },
                    )
                },
            ) {
                Text(
                    text = "Login",
                    style = WithpeaceTheme.typography.body,
                    color = WithpeaceTheme.colors.MainPink,
                )
            }
        }
    }
}

@Preview(widthDp = 400, heightDp = 900)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}

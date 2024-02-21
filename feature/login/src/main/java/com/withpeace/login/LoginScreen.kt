package com.withpeace.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoginScreen(){
    Surface(modifier = Modifier.fillMaxSize()) {
        Box{
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Login")
            }
        }
    }
}

@Preview(widthDp = 400, heightDp = 900)
@Composable
fun LoginScreenPreview(){
    LoginScreen()
}
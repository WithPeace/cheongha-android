package com.withpeace.withpeace

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.withpeace.withpeace.core.data.repository.DefaultTokenRepository
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import com.withpeace.withpeace.feature.login.LoginScreen
import com.withpeace.withpeace.ui.theme.WithpeaceTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: TokenRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repository.googleLogin {
                Log.e("woogi", "onCreate: hello", )
            }.collect{
                Log.d("woogi", "token: $it")
            }
        }

        setContent {
            WithpeaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

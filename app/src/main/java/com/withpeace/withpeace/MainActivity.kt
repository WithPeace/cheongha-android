package com.withpeace.withpeace

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.feature.login.navigation.LOGIN_ROUTE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // System Bar에 가려지는 뷰 영역을 개발자가 제어하겠다.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.isLogin.collect { isLogin ->
                    if(isLogin) {
                        Log.d("covy","Main 화면으로 이동")
                        composeStart(LOGIN_ROUTE)
                    } else {
                        composeStart(LOGIN_ROUTE)
                    }
                    delay(2000L)
                    splashScreen.setKeepOnScreenCondition { false }
                }
            }
        }
    }
    private fun ComponentActivity.composeStart(startDestination: String) {
        setContent {
            WithpeaceTheme {
                WithpeaceApp(startDestination = startDestination)
            }
        }
    }
}

package com.withpeace.withpeace

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.withpeace.withpeace.core.analytics.AnalyticsHelper
import com.withpeace.withpeace.core.analytics.LocalAnalyticsHelper
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.feature.home.navigation.HOME_ROUTE
import com.withpeace.withpeace.feature.login.navigation.LOGIN_ROUTE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var splashScreen: SplashScreen

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // System Bar에 가려지는 뷰 영역을 개발자가 제어하겠다.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
            // 처음 앱 켰을때만 2초기다림, 화면회전에는 기다리면 안됨
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        MainUiState.Home -> composeStart(HOME_ROUTE)
                        MainUiState.Login -> composeStart(LOGIN_ROUTE)
                        MainUiState.Update -> {
                            compulsionUpdate()
                        }

                        MainUiState.Error -> finish()
                        MainUiState.Loading -> {}
                    }
                    splashScreen.setKeepOnScreenCondition { false }
                }
            }
        }
    }

    private fun ComponentActivity.composeStart(startDestination: String) {
        setContent {
            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper,
            ) {
                WithpeaceTheme {
                    WithpeaceApp(startDestination = startDestination)
                }
            }
        }
    }
    private fun compulsionUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startUpdateFlow(appUpdateManager, appUpdateInfo)
            } else {
                Toast.makeText(this, "앱을 사용하려면 업데이트가 필요해요!", Toast.LENGTH_LONG).show()
                redirectToPlayStore()
            }
        }
    }

    private fun startUpdateFlow(appUpdateManager: AppUpdateManager, appUpdateInfo: AppUpdateInfo) {
        val updateActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
                if (result.resultCode != RESULT_OK) {
                    finish()
                }
            }

        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                updateActivityResultLauncher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun redirectToPlayStore() {
        val packageName = packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: Exception) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName"),
                ),
            )
        }
        finish()
    }
}

// Update 로직, WebView 적용(일단 임시), SwipeRefresh
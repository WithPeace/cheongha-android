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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    private val updateActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            if (result.resultCode != RESULT_OK) {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // System Bar에 가려지는 뷰 영역을 개발자가 제어하겠다.
        WindowCompat.setDecorFitsSystemWindows(window, false)
        lifecycleScope.launch {
            splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        MainUiState.Home -> composeStart(HOME_ROUTE)
                        MainUiState.Login -> composeStart(LOGIN_ROUTE)
                        MainUiState.Update -> {
                            showForceUpdateDialog()
                        }

                        MainUiState.Error -> {
                            //TODO 에러 화면으로 이동
                            Toast.makeText(
                                this@MainActivity,
                                "네트워크 상태가 원활하지 않습니다.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            finish() // 강제 업데이트
                        }
                        MainUiState.Loading -> {}
                    }
                    splashScreen.setKeepOnScreenCondition { false }
                }
            }
        }
    }

    private fun showForceUpdateDialog() {
        setContent {
            WithpeaceTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WithpeaceTheme.colors.SystemWhite),
                ) {
                    Dialog(
                        onDismissRequest = {

                        },
                    ) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(WithpeaceTheme.colors.SystemWhite)
                                .padding(24.dp),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.background(WithpeaceTheme.colors.SystemWhite),
                            ) {
                                Text(
                                    color = WithpeaceTheme.colors.SystemBlack,
                                    text = "새로운 버전이 업데이트 되었어요",
                                    textAlign = TextAlign.Center,
                                    style = WithpeaceTheme.typography.title2,
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    color = WithpeaceTheme.colors.SystemGray1,
                                    text = "더 나아진 청하를 이용하기 위해\n 지금 업데이트 해주세요.",
                                    textAlign = TextAlign.Center,
                                    style = WithpeaceTheme.typography.body,
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                TextButton(
                                    onClick = {
                                        redirectToPlayStore()
                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(WithpeaceTheme.colors.MainPurple)
                                        .width(264.dp),
                                ) {
                                    Text(
                                        color = WithpeaceTheme.colors.SystemWhite,
                                        text = "업데이트하기",
                                        textAlign = TextAlign.Center,
                                        style = WithpeaceTheme.typography.body,
                                    )
                                }
                            }
                        }
                    }
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
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startUpdateFlow(appUpdateManager, appUpdateInfo)
            } else {
                Toast.makeText(this, "앱을 사용하려면 업데이트가 필요해요!", Toast.LENGTH_LONG).show()
                // redirectToPlayStore()
            }
        }
    }

    private fun startUpdateFlow(appUpdateManager: AppUpdateManager, appUpdateInfo: AppUpdateInfo) {
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

    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateActivityResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                    )
                }
            }
    }
}

package com.withpeace.withpeace.core.permission

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

class ImagePermissionHelper(
    private val context: Context,
) {
    fun onCheckSelfImagePermission(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit,
    ) {
        when {
            isOverThirteenImageGranted() -> onPermissionGranted()
            isOverFourteenImagePartialGranted() -> onPermissionGranted()
            isLessThanTwelveImageGranted() -> onPermissionGranted()
            else -> onPermissionDenied()
        }
    }

    @Composable
    fun getImageLauncher(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit,
    ): ManagedActivityResultLauncher<String, Boolean> =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) onPermissionGranted()
            else onPermissionDenied()
        }

    fun requestPermissionDialog(
        launcher: ManagedActivityResultLauncher<String, Boolean>,
    ) {
        when {
            isLessThanTwelve -> launcher.launch(READ_EXTERNAL_STORAGE)
            isOverFourteen -> launcher.launch(READ_MEDIA_IMAGES)
            isOverThirteen -> launcher.launch(READ_MEDIA_IMAGES)
            else -> {}
        }
    }

    private val isLessThanTwelve = Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2
    private val isOverThirteen = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    private val isOverFourteen = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE

    private fun isLessThanTwelveImageGranted() =
        isLessThanTwelve && ContextCompat.checkSelfPermission(
            context,
            READ_EXTERNAL_STORAGE,
        ) == PERMISSION_GRANTED

    private fun isOverThirteenImageGranted() =
        isOverThirteen && (
            ContextCompat.checkSelfPermission(
                context,
                READ_MEDIA_IMAGES,
            ) == PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    READ_MEDIA_VIDEO,
                ) == PERMISSION_GRANTED
            )

    private fun isOverFourteenImagePartialGranted() =
        isOverFourteen &&
            ContextCompat.checkSelfPermission(
                context,
                READ_MEDIA_VISUAL_USER_SELECTED,
            ) == PERMISSION_GRANTED

    @Composable
    fun ImagePermissionDialog(
        onDismissRequest: () -> Unit,
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .background(
                        WithpeaceTheme.colors.SystemWhite,
                        RoundedCornerShape(10.dp),
                    )
                    .fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.padding(24.dp),
                    text = "저장공간 권한이 꺼져있습니다.\n" +
                        "사진 업로드를 위해서는 [권한] 설정에서\n" +
                        "저장공간 권한을 허용해야합니다.",
                    style = WithpeaceTheme.typography.caption,
                )
                Row(modifier = Modifier.align(Alignment.End)) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                onDismissRequest()
                            }
                            .padding(end = 16.dp, bottom = 16.dp),
                        text = "취소",
                        style = WithpeaceTheme.typography.caption,
                        color = WithpeaceTheme.colors.SubBlue2,
                    )
                    Text(
                        modifier = Modifier
                            .clickable {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.data = Uri.parse("package:${context.packageName}")
                                context.startActivity(intent)
                                onDismissRequest()
                            }
                            .padding(start = 16.dp, end = 30.dp, bottom = 16.dp),
                        text = "설정으로 가기",
                        style = WithpeaceTheme.typography.caption,
                        color = WithpeaceTheme.colors.SubBlue2,
                    )
                }
            }
        }
    }
}


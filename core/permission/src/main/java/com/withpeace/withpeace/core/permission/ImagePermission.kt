package com.withpeace.withpeace.core.permission

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat

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
}


package com.withpeace.withpeace.core.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.permission.ImagePermissionHelper
import com.withpeace.withpeace.core.ui.R

@Composable
fun ProfileImageEditor(
    profileImage: String?,
    modifier: Modifier,
    onNavigateToGallery: () -> Unit,
    contentDescription: String
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val imagePermissionHelper = remember { ImagePermissionHelper(context) }
    val launcher = imagePermissionHelper.getImageLauncher(
        onPermissionGranted = onNavigateToGallery,
        onPermissionDenied = { showDialog = true },
    )
    if (showDialog) {
        imagePermissionHelper.ImagePermissionDialog { showDialog = false }
    }
    val imageModifier = modifier
        .size(120.dp)
        .clip(CircleShape)
    Row(
        modifier = modifier.wrapContentSize(Alignment.Center),
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier.clickable {
                imagePermissionHelper.onCheckSelfImagePermission(
                    onPermissionGranted = onNavigateToGallery,
                    onPermissionDenied = {
                        imagePermissionHelper.requestPermissionDialog(launcher)
                    },
                )
            },
        ) {
            GlideImage(
                modifier = imageModifier,
                imageModel = { profileImage },
                failure = {
                    Image(
                        painterResource(id = R.drawable.ic_default_profile),
                        modifier = imageModifier,
                        contentDescription = contentDescription,
                    )
                },
            )
            Image(
                modifier = modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 6.dp, end = 6.dp),
                painter = painterResource(id = R.drawable.ic_editor_pencil),
                contentDescription = contentDescription,
            )
        }
    }
}
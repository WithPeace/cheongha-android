package com.withpeace.withpeace.core.imagestorage

import android.net.Uri

data class ImageInfoEntity(
    val imageUri: Uri,
    val mimeType: String,
    val size: Long,
)
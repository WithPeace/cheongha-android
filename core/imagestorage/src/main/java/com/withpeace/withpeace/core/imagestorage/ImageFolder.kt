package com.withpeace.withpeace.core.imagestorage

import android.net.Uri

data class ImageFolderEntity(
    val folderName: String,
    val representativeImageUri: Uri,
    val count: Int,
)

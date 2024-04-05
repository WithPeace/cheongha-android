package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.image.ImageInfo
import com.withpeace.withpeace.core.imagestorage.ImageInfoEntity

fun ImageInfoEntity.toDomain(): ImageInfo {
    return ImageInfo(
        uri = imageUri.toString(),
        mimeType = mimeType,
        size = size,
    )
}

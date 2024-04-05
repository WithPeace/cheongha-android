package com.withpeace.withpeace.core.domain.model.image

data class ImageInfo(
    val uri: String,
    val mimeType: String,
    val size: Long,
) {
    fun isUploadType(): Boolean {
        val imageMimeType = mimeType.split("/").last().lowercase() // image/png
        return uploadType.contains(imageMimeType)
    }

    fun isSizeOver(): Boolean {
        return bytesToMegabytes(size) > TEN_MEGA_BYTE
    }

    private fun bytesToMegabytes(bytes: Long): Double {
        return bytes / (BYTE_TO_KB_UNIT * KB_TO_MB_UNIT)
    }

    companion object {
        private val uploadType = arrayOf(
            "jpg", "png", "webp", "jpeg",
        )
        private const val BYTE_TO_KB_UNIT = 1024.0
        private const val KB_TO_MB_UNIT = 1024.0
        private const val TEN_MEGA_BYTE = 10
    }
}

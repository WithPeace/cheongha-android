package com.withpeace.withpeace.core.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun Uri.convertToFile(context: Context): File {
    val bitmap = convertToBitmap(context)
    val file = bitmap?.convertToFile(context)
    return file ?: throw IllegalStateException("이미지를 파일로 바꾸는데에 실패하였습니다")
}

private fun Uri.convertToBitmap(context: Context): Bitmap? {
    return context.contentResolver.openInputStream(this)?.use {
        BitmapFactory.decodeStream(it)
    }
}

private fun Bitmap.convertToFile(context: Context): File {
    val tempFile = File.createTempFile("imageFile", ".jpg", context.cacheDir)

    FileOutputStream(tempFile).use { fileOutputStream ->
        compress(Bitmap.CompressFormat.JPEG, HIGHEST_COMPRESS_QUALITY, fileOutputStream)
    }
    return tempFile
}

private const val HIGHEST_COMPRESS_QUALITY = 100

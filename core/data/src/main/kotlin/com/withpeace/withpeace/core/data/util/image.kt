package com.withpeace.withpeace.core.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.webkit.URLUtil
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.net.URL


fun String.convertToFile(context: Context): File {
    return if (URLUtil.isHttpUrl(this)) {
        return URL(this).convertToFile(context)
    } else {
        Uri.parse(this).convertToFile(context)
    }
}

fun URL.convertToFile(context: Context): File {
    val bitmap = convertToBitmap()
    val file = bitmap.convertToFile(context)
    file.updateExifOrientation(this)
    return file
}

fun URL.convertToBitmap(): Bitmap {
    val stream = openStream()
    return BitmapFactory.decodeStream(stream)
}

fun Uri.convertToFile(context: Context): File {
    val bitmap = convertToBitmap(context)
    val file = bitmap?.convertToFile(context)
    file?.updateExifOrientation(context,this)
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

private fun File.updateExifOrientation(context: Context, uri: Uri) {
    context.contentResolver.openInputStream(uri)?.use {
        val exif = ExifInterface(it)
        exif.getAttribute(ExifInterface.TAG_ORIENTATION)?.let { attribute ->
            val newExif = ExifInterface(absolutePath)
            newExif.setAttribute(ExifInterface.TAG_ORIENTATION, attribute)
            newExif.saveAttributes()
        }
    }
}

private fun File.updateExifOrientation(url: URL) {
    url.openStream()?.use {
        val exif = ExifInterface(it)
        exif.getAttribute(ExifInterface.TAG_ORIENTATION)?.let { attribute ->
            val newExif = ExifInterface(absolutePath)
            newExif.setAttribute(ExifInterface.TAG_ORIENTATION, attribute)
            newExif.saveAttributes()
        }
    }
}

private const val HIGHEST_COMPRESS_QUALITY = 20

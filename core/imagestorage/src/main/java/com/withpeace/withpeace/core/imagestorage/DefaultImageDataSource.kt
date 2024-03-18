package com.withpeace.withpeace.core.imagestorage

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Images
import androidx.core.os.bundleOf

class DefaultImageDataSource(
    private val context: Context,
) : ImageDataSource {

    private val uriExternal: Uri by lazy { // 버젼에 따라 외부 이미지 파일에 접근하는 uri가 다름.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            Images.Media.EXTERNAL_CONTENT_URI
        }
    }

    private val sortedOrder = Images.ImageColumns.DATE_TAKEN

    override suspend fun getImages(
        page: Int,
        loadSize: Int,
        folder: String?,
    ): List<Uri> {
        val imageUris = mutableListOf<Uri>()
        val pagingImagesQuery = context.getPagingImagesQuery((page - 1) * loadSize, loadSize, folder)
        pagingImagesQuery.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(Images.Media._ID)
            while (cursor.moveToNext()) {
                val uri = ContentUris.withAppendedId(uriExternal, cursor.getLong(idColumn))
                imageUris.add(uri)
            }
            cursor.close()
        }
        return imageUris
    }

    override suspend fun getFolders(): List<ImageFolderEntity> {
        val folderList = mutableListOf<ImageFolderEntity>()
        val folderQuery = context.getFolderQuery()
        folderQuery.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(Images.Media._ID)
            val bucketNameColumn = cursor.getColumnIndexOrThrow(Images.Media.BUCKET_DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val uri = ContentUris.withAppendedId(uriExternal, cursor.getLong(idColumn))
                val folderName = cursor.getString(bucketNameColumn)
                val matchedFolder = folderList.find { it.folderName == folderName }
                if (matchedFolder == null) { //처음 등장하는 폴더 이름이면 리스트에 추가
                    folderList.add(ImageFolderEntity(folderName, uri, 1))
                } else { // 아니라면 Count값을 증가
                    val index = folderList.indexOf(matchedFolder)
                    folderList[index] = matchedFolder.copy(count = matchedFolder.count + 1)
                }
            }
            cursor.close()
        }
        return folderList
    }

    private fun Context.getPagingImagesQuery(
        offset: Int?,
        limit: Int?,
        folder: String?,
    ): Cursor {
        val projection = arrayOf(
            Images.ImageColumns.DATA,
            Images.Media._ID,
        )
        val selection = folder?.let { "${Images.Media.DATA} LIKE ?" }
        val selectionArgs = folder?.let { arrayOf("%$folder%") }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val bundle = bundleOf(
                ContentResolver.QUERY_ARG_OFFSET to offset,
                ContentResolver.QUERY_ARG_LIMIT to limit,
                ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED),
                ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
                ContentResolver.QUERY_ARG_SQL_SELECTION to selection,
                ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS to selectionArgs,
            )
            return contentResolver.query(uriExternal, projection, bundle, null)
                ?: throw IllegalStateException("이미지 커서를 가져올 수 없어요")
        } else {
            return contentResolver.query(
                uriExternal,
                projection,
                selection,
                selectionArgs,
                "$sortedOrder DESC LIMIT $limit OFFSET $offset",
            ) ?: throw IllegalStateException("이미지 커서를 가져올 수 없어요")
        }
    }

    private fun Context.getFolderQuery(): Cursor {
        val projection = arrayOf(
            Images.Media._ID,
            Images.Media.BUCKET_DISPLAY_NAME,
        )
        val selection = null
        val selectionArgs = null
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val bundle = bundleOf(
                ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED),
                ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
                ContentResolver.QUERY_ARG_SQL_SELECTION to selection,
                ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS to selectionArgs,
            )
            return contentResolver.query(uriExternal, projection, bundle, null)
                ?: throw IllegalStateException("이미지 커서를 가져올 수 없어요")
        } else {
            return contentResolver.query(
                uriExternal,
                projection,
                selection,
                selectionArgs,
                "${Images.Media.DATE_ADDED} DESC",
            ) ?: throw IllegalStateException("이미지를 가져올 수 없어요.")
        }
    }
}

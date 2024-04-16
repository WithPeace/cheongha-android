package com.withpeace.withpeace.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.withpeace.withpeace.core.data.paging.ImagePagingSource
import com.withpeace.withpeace.core.domain.model.image.ImageFolder
import com.withpeace.withpeace.core.domain.model.image.ImageInfo
import com.withpeace.withpeace.core.domain.repository.ImageRepository
import com.withpeace.withpeace.core.imagestorage.ImageDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultImageRepository @Inject constructor(
    private val imageDataSource: ImageDataSource,
) : ImageRepository {
    override suspend fun getFolders(): List<ImageFolder> =
        withContext(Dispatchers.IO) {
            imageDataSource.getFolders().map {
                ImageFolder(
                    it.folderName,
                    it.representativeImageUri.toString(),
                    it.count,
                )
            }
        }

    override fun getImages(
        folderName: String?,
    ): Flow<PagingData<ImageInfo>> =
        Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = {
                ImagePagingSource(
                    imageDataSource = imageDataSource,
                    folderName = folderName,
                    pageSize = PAGE_SIZE,
                )
            },
        ).flow

    companion object {
        private const val PAGE_SIZE = 30
    }
}

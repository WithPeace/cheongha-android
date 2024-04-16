package com.withpeace.withpeace.core.domain.repository

import androidx.paging.PagingData
import com.withpeace.withpeace.core.domain.model.image.ImageFolder
import com.withpeace.withpeace.core.domain.model.image.ImageInfo
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun getFolders(): List<ImageFolder>

    fun getImages(
        folderName: String?,
    ): Flow<PagingData<ImageInfo>>
}

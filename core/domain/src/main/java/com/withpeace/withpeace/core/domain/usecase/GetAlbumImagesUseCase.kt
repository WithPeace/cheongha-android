package com.withpeace.withpeace.core.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.withpeace.withpeace.core.domain.paging.DefaultImagePagingSource
import com.withpeace.withpeace.core.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
) {
    operator fun invoke(selectedFolderName: String): Flow<PagingData<String>> {
        var folderName: String? = selectedFolderName
        if (folderName == "최근 항목") folderName = null
        return Pager(
            config = PagingConfig(
                pageSize = 9841,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = {
                DefaultImagePagingSource(
                    imageRepository = imageRepository,
                    folderName = folderName,
                )
            },
        ).flow
    }
}

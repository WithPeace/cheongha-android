package com.withpeace.withpeace.core.domain.usecase

import androidx.paging.PagingData
import com.withpeace.withpeace.core.domain.model.image.ImageInfo
import com.withpeace.withpeace.core.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
) {
    operator fun invoke(selectedFolderName: String): Flow<PagingData<ImageInfo>> =
        imageRepository.getImages(selectedFolderName)
}

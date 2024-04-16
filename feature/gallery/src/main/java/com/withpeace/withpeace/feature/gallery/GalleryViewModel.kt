package com.withpeace.withpeace.feature.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.withpeace.withpeace.core.domain.model.image.ImageFolder
import com.withpeace.withpeace.core.domain.model.image.ImageInfo
import com.withpeace.withpeace.core.domain.model.image.LimitedImages
import com.withpeace.withpeace.core.domain.usecase.GetAlbumImagesUseCase
import com.withpeace.withpeace.core.domain.usecase.GetAllFoldersUseCase
import com.withpeace.withpeace.feature.gallery.navigation.GALLERY_ALREADY_IMAGE_COUNT_ARGUMENT
import com.withpeace.withpeace.feature.gallery.navigation.GALLERY_IMAGE_LIMIT_ARGUMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getAlbumImagesUseCase: GetAlbumImagesUseCase,
) : ViewModel() {

    private val _selectedImages =
        MutableStateFlow(
            LimitedImages(
                urls = emptyList(),
                maxCount = savedStateHandle.get<Int>(GALLERY_IMAGE_LIMIT_ARGUMENT)
                    ?: DEFAULT_MAX_SELECTABLE_COUNT,
                alreadyExistCount = savedStateHandle.get<Int>(GALLERY_ALREADY_IMAGE_COUNT_ARGUMENT)
                    ?: DEFAULT__CURRENT_IMAGE_COUNT,
            ),
        )
    val selectedImages = _selectedImages.asStateFlow()

    private val _allFolders = MutableStateFlow<List<ImageFolder>>(emptyList())
    val allFolders = _allFolders.asStateFlow()

    private val _selectedFolder = MutableStateFlow<ImageFolder?>(null)
    val selectedFolder = _selectedFolder.asStateFlow()

    val images = selectedFolder.map { imageFolder ->
        getImagePagingData(imageFolder?.folderName ?: "")
    }

    private val _sideEffect = Channel<GallerySideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            _allFolders.update { getAllFoldersUseCase() }
        }
    }

    private suspend fun getImagePagingData(folderName:String):PagingData<ImageInfo>{
        return getAlbumImagesUseCase(folderName,PAGE_SIZE)
            .cachedIn(viewModelScope) // Paging 정보를 화면 회전에도 날라가지 않게 하기 위함
            .firstOrNull() ?: PagingData.empty()
    }

    fun onSelectFolder(imageFolder: ImageFolder?) {
        _selectedFolder.value = imageFolder
    }

    fun onSelectImage(imageInfo: ImageInfo) {
        when {
            selectedImages.value.contains(imageInfo.uri) -> _selectedImages.update {
                it.deleteImage(imageInfo.uri)
            }

            selectedImages.value.canAddImage() -> {
                if (!imageInfo.isUploadType()) {
                    viewModelScope.launch { _sideEffect.send(GallerySideEffect.SelectImageNoApplyType) }
                    return
                }
                if (imageInfo.isSizeOver()) {
                    viewModelScope.launch { _sideEffect.send(GallerySideEffect.SelectImageOverSize) }
                    return
                }
                _selectedImages.update { it.addImage(imageInfo.uri) }
            }

            else -> viewModelScope.launch { _sideEffect.send(GallerySideEffect.SelectImageNoMore) }
        }
    }

    companion object {
        private const val DEFAULT_MAX_SELECTABLE_COUNT = 5
        private const val DEFAULT__CURRENT_IMAGE_COUNT = 0
        private const val PAGE_SIZE = 30
    }
}

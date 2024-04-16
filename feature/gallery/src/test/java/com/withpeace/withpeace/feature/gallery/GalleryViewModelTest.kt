package com.withpeace.withpeace.feature.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.image.ImageFolder
import com.withpeace.withpeace.core.domain.model.image.ImageInfo
import com.withpeace.withpeace.core.domain.model.image.LimitedImages
import com.withpeace.withpeace.core.domain.usecase.GetAlbumImagesUseCase
import com.withpeace.withpeace.core.domain.usecase.GetAllFoldersUseCase
import com.withpeace.withpeace.core.testing.MainDispatcherRule
import com.withpeace.withpeace.feature.gallery.navigation.GALLERY_ALREADY_IMAGE_COUNT_ARGUMENT
import com.withpeace.withpeace.feature.gallery.navigation.GALLERY_IMAGE_LIMIT_ARGUMENT
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class GalleryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: GalleryViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private val getAllFoldersUseCase = mockk<GetAllFoldersUseCase>(relaxed = true)
    private val getAlbumImagesUseCase = mockk<GetAlbumImagesUseCase>(relaxed = true)

    private fun savedStateHandle(
        alreadyImageCount: Int,
        limitImageCount: Int,
    ): SavedStateHandle = SavedStateHandle(
        mapOf(
            GALLERY_ALREADY_IMAGE_COUNT_ARGUMENT to alreadyImageCount,
            GALLERY_IMAGE_LIMIT_ARGUMENT to limitImageCount,
        ),
    )

    private fun viewModel() =
        GalleryViewModel(savedStateHandle, getAllFoldersUseCase, getAlbumImagesUseCase)

    @Test
    fun `이미 선택된 이미지 개수와 최대 이미지 개수 설정이 가능하다`() {
        // given
        savedStateHandle = savedStateHandle(1, 5)
        // when
        viewModel = viewModel()
        // then
        val actual = viewModel.selectedImages.value
        assertThat(actual).isEqualTo(LimitedImages(emptyList(), 5, 1))
    }

    @Test
    fun `모든 이미지 폴더를 가져올 수 있다`() {
        // given
        savedStateHandle = SavedStateHandle()
        val testFolders = List(10) {
            ImageFolder(
                "test",
                representativeImageUri = "test",
                imageCount = 10,
            )
        }
        coEvery { getAllFoldersUseCase() } returns testFolders
        // when
        viewModel = viewModel()
        // then
        val actual = viewModel.allFolders.value
        assertThat(actual).isEqualTo(testFolders)
    }

    @Test
    fun `폴더를 선택할 수 있다`() {
        // given
        savedStateHandle = SavedStateHandle()
        viewModel = viewModel()
        val testFolder = ImageFolder(
            folderName = "test",
            representativeImageUri = "test",
            imageCount = 10,
        )
        // when
        viewModel.onSelectFolder(testFolder)
        // then
        val actual = viewModel.selectedFolder.value
        assertThat(actual).isEqualTo(testFolder)
    }

    @Test
    fun `폴더를 선택하지 않으면, 이미지 상태는 비어있다`() = runTest {
        // given
        savedStateHandle = SavedStateHandle()
        viewModel = viewModel()
        coEvery {
            getAlbumImagesUseCase("")
        } returns Pager(
            config = PagingConfig(30),
            pagingSourceFactory = emptyList<ImageInfo>().asPagingSourceFactory(),
        ).flow
        // when & then
        val actual = viewModel.images.getFullScrollItems()
        assertThat(actual).isEqualTo(emptyList<String>())
    }

    @Test
    fun `폴더를 변경하면 이미지 상태를 가져올 수 있다`() = runTest {
        // given
        savedStateHandle = SavedStateHandle()
        viewModel = viewModel()
        val testFolder = ImageFolder(
            folderName = "test",
            representativeImageUri = "test",
            imageCount = 10,
        )
        val testImages = List(100) { ImageInfo(
            "uri","type",0L
        )}
        coEvery {
            getAlbumImagesUseCase(testFolder.folderName)
        } returns Pager(
            config = PagingConfig(30),
            pagingSourceFactory = testImages.asPagingSourceFactory(),
        ).flow
        // when
        viewModel.onSelectFolder(testFolder)
        val actual = viewModel.images.getFullScrollItems()
        assertThat(actual).isEqualTo(testImages)
    }

    private suspend fun <T : Any> Flow<PagingData<T>>.getFullScrollItems() =
        asSnapshot { appendScrollWhile { true } }

    @Test
    fun `이미지를 선택할 수 있다`() {
        // given
        savedStateHandle = SavedStateHandle()
        viewModel = viewModel()
        val testImage = ImageInfo(
            "uri","images/png",10
        )
        // when
        viewModel.onSelectImage(testImage)
        // then
        val actual = viewModel.selectedImages.value.contains(testImage.uri)
        assertThat(actual).isTrue()
    }

    @Test
    fun `이미 선택한 이미지를 선택하면, 해제할 수 있다`() {
        // given
        savedStateHandle = SavedStateHandle()
        viewModel = viewModel()
        val testImage = ImageInfo(
            "uri","type",0L
        )
        // when
        viewModel.onSelectImage(testImage)
        viewModel.onSelectImage(testImage)
        // then
        val actual = viewModel.selectedImages.value.contains(testImage.uri)
        assertThat(actual).isFalse()
    }

    @Test
    fun `최대 이미지 개수룰 넘어버렸을 때 이미지를 선택하면, 선택 실패 이팩트가 발생한다`() = runTest {
        // given
        savedStateHandle = SavedStateHandle(
            mapOf(GALLERY_IMAGE_LIMIT_ARGUMENT to 0), //최대 이미지 개수 0
        )
        viewModel = viewModel()
        val testImage = ImageInfo(
            "uri","type",0L
        )
        // when && then
        viewModel.sideEffect.test {
            viewModel.onSelectImage(testImage)
            val actual = awaitItem()
            assertThat(actual).isEqualTo(GallerySideEffect.SelectImageNoMore)
        }
    }
}

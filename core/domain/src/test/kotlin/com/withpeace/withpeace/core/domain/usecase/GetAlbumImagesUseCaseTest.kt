package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.ImageRepository
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test


class GetAlbumImagesUseCaseTest {

    private lateinit var getAlbumImagesUseCase: GetAlbumImagesUseCase
    private val imageRepository = mockk<ImageRepository>(relaxed = true)

    @Test
    fun `이미지 페이징 정보를 요청한다`() = runTest() {
        // given
        getAlbumImagesUseCase = GetAlbumImagesUseCase(imageRepository)
        // when
        getAlbumImagesUseCase("test",20)
        // then
        verify { imageRepository.getImages("test",20) }
    }
}

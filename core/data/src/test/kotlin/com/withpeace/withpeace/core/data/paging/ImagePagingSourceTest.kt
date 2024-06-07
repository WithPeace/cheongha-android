package com.withpeace.withpeace.core.data.paging


import android.net.Uri
import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.domain.model.image.ImageInfo
import com.withpeace.withpeace.core.imagestorage.ImageDataSource
import com.withpeace.withpeace.core.imagestorage.ImageInfoEntity
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ImagePagingSourceTest {

    private lateinit var imagePagingSource: ImagePagingSource
    private val imageDataSource = mockk<ImageDataSource>(relaxed = true)
    private var mockImageInfoList = listOf<ImageInfo>()

    @Before
    fun setup() {
        mockImageInfoList = listOf()
        val uri = mockingUri()
        coEvery { imageDataSource.getImages(any(), any(), any()) } returns List(20) {
            ImageInfoEntity(
                imageUri = uri,
                mimeType = "jpg",
                byteSize = 100,
            )
        }.apply {
            mockImageInfoList = mockImageInfoList + this.map { it.toDomain() }
        }
    }

    @Test
    fun `refresh 테스트`() = runTest {
        // when
        imagePagingSource = ImagePagingSource(imageDataSource, "test", 20)
        val pager = TestPager(PagingConfig(20), imagePagingSource)
        val result = pager.refresh() as LoadResult.Page
        // then
        assertThat(result.data).containsExactlyElementsIn(
            List(20) {
                ImageInfo(
                    uri = "",
                    mimeType = "jpg",
                    byteSize = 100,
                )
            },
        ).inOrder()
    }

    @Test
    fun `append 테스트`() = runTest {
        // when
        imagePagingSource = ImagePagingSource(imageDataSource, "test", 20)
        val pager = TestPager(PagingConfig(20), imagePagingSource)
        var result = listOf<ImageInfo>()
        result = result + (pager.refresh() as LoadResult.Page) + (pager.append() as LoadResult.Page)
        // then
        assertThat(result).containsExactlyElementsIn(
            List(40) {
                ImageInfo(
                    uri = "",
                    mimeType = "jpg",
                    byteSize = 100,
                )
            },
        ).inOrder()
    }

    private fun mockingUri(): Uri {
        mockkStatic(Uri::class)
        val uri = mockk<Uri>()
        every { uri.toString() } returns ""
        return uri
    }
}

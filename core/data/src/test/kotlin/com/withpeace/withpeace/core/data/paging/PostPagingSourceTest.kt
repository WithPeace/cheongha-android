package com.withpeace.withpeace.core.data.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import com.withpeace.withpeace.core.network.di.response.post.PostResponse
import com.withpeace.withpeace.core.network.di.response.post.PostTopicResponse
import com.withpeace.withpeace.core.network.di.service.PostService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class PostPagingSourceTest {
    private lateinit var postPagingSource: PostPagingSource
    private val postService = mockk<PostService>()
    private val userRepository = mockk<UserRepository>(relaxed = true)

    @Before
    fun setup() {
        coEvery {
            postService.getPosts(
                postTopic = any(),
                pageIndex = any(),
                pageSize = any(),
            )
        } returns ApiResponse.Success<BaseResponse<List<PostResponse>>>(
            response = Response.success(
                BaseResponse(
                    data = List(20) {
                        PostResponse(
                            postId = 0,
                            title = "title",
                            content = "content",
                            type = PostTopicResponse.FREEDOM,
                            postImageUrl = null,
                            createDate = "2024/04/12 00:00:00",
                        )
                    },
                    error = null,
                ),
            ),
        )
    }

    @Test
    fun `refrsh 테스트`() = runTest {
        // given
        postPagingSource = PostPagingSource(
            postService,
            postTopic = PostTopic.FREEDOM,
            pageSize = 20,
            onError = {},
            userRepository = userRepository
        )
        // when
        val pager = TestPager(PagingConfig(20), postPagingSource)
        val result = pager.refresh() as LoadResult.Page
        // then
        assertThat(result.data).containsExactlyElementsIn(
            List(20) {
                Post(
                    postId = 0,
                    title = "title",
                    content = "content",
                    postTopic = PostTopic.FREEDOM,
                    postImageUrl = null,
                    createDate = Date(
                        LocalDateTime.of(
                            LocalDate.of(2024, 4, 12),
                            LocalTime.of(0, 0, 0),
                        ),
                    ),
                )
            },
        ).inOrder()
    }

    @Test
    fun `append 테스트`() = runTest {
        // given
        postPagingSource = PostPagingSource(
            postService,
            postTopic = PostTopic.FREEDOM,
            pageSize = 20,
            onError = {},
            userRepository = userRepository
        )
        // when
        var result = listOf<Post>()
        val pager = TestPager(PagingConfig(20), postPagingSource)
        result =
            result + (pager.refresh() as LoadResult.Page).data + (pager.append() as LoadResult.Page).data
        // then
        assertThat(result).containsExactlyElementsIn(
            List(40) {
                Post(
                    postId = 0,
                    title = "title",
                    content = "content",
                    postTopic = PostTopic.FREEDOM,
                    postImageUrl = null,
                    createDate = Date(
                        LocalDateTime.of(
                            LocalDate.of(2024, 4, 12),
                            LocalTime.of(0, 0, 0),
                        ),
                    ),
                )
            },
        ).inOrder()
    }
}

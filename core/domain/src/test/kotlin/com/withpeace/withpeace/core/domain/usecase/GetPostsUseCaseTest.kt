package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.repository.PostRepository
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetPostsUseCaseTest {
    private lateinit var getPostsUseCase: GetPostsUseCase
    private val postRepository = mockk<PostRepository>(relaxed = true)

    @Test
    fun `게시글 페이징 정보를 요청한다`() = runTest() {
        // given
        val mockError = mockk<suspend (CheonghaError) -> Unit>()
        getPostsUseCase = GetPostsUseCase(postRepository)
        // when
        getPostsUseCase(postTopic = PostTopic.INFORMATION, pageSize = 0, onError = mockError)
        // then
        verify {
            postRepository.getPosts(
                postTopic = PostTopic.INFORMATION,
                pageSize = 0,
                onError = mockError,
            )
        }
    }
}

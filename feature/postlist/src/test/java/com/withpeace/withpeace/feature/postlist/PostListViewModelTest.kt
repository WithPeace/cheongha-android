package com.withpeace.withpeace.feature.postlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.usecase.GetPostsUseCase
import com.withpeace.withpeace.core.testing.MainDispatcherRule
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel
import com.withpeace.withpeace.core.ui.post.toPostUiModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class PostListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var postListViewModel: PostListViewModel
    private val getPostsUseCase = mockk<GetPostsUseCase>(relaxed = true)

    @Test
    fun `주제를 바꿀 수 있다`() = runTest {
        // given
        postListViewModel = PostListViewModel(getPostsUseCase)
        // when
        postListViewModel.onTopicChanged(PostTopicUiModel.ECONOMY)
        // then
        val actual = postListViewModel.currentTopic.value
        assertThat(actual).isEqualTo(PostTopicUiModel.ECONOMY)
    }

    @Test
    fun `게시글 리스트를 가져올 수 있다`() = runTest {
        // given
        val testPosts = List(100) {
            Post(
                postId = it.toLong(),
                title = "",
                content = "",
                postTopic = PostTopic.FREEDOM,
                createDate = Date(date = LocalDateTime.MIN),
                postImageUrl = null,
            )
        }
        coEvery {
            getPostsUseCase(
                postTopic = PostTopic.FREEDOM,
                pageSize = any(),
                onError = any(),
            )
        } returns Pager(
            config = PagingConfig(7),
            pagingSourceFactory = testPosts.asPagingSourceFactory(),
        ).flow
        // when && then
        postListViewModel = PostListViewModel(getPostsUseCase)
        val actual = postListViewModel.postListPagingFlow.getFullScrollItems()
        assertThat(actual).isEqualTo(testPosts.map { it.toPostUiModel() })
    }

    @Test
    fun `게시글 목록을 가져올 때, 네트워크에 문제가 있다면 네트워크 에러 이벤트가 발생한다`() = runTest {
        // given
        val errorSlot = slot<suspend (CheonghaError) -> Unit>()
        coEvery {
            getPostsUseCase(
                postTopic = any(),
                pageSize = any(),
                onError = capture(errorSlot),
            )
        } returns flow<PagingData<Post>> {
            errorSlot.captured.invoke(ResponseError.INVALID_TOKEN_ERROR)
        }.catch {  }
        // when
        postListViewModel = PostListViewModel(getPostsUseCase)
        // then
        postListViewModel.uiEvent.test {
            val actual = awaitItem()
            assertThat(actual).isEqualTo(PostListUiEvent.NetworkError)
        }
    }

    private suspend fun <T : Any> Flow<PagingData<T>>.getFullScrollItems() =
        asSnapshot { appendScrollWhile { true } }

}

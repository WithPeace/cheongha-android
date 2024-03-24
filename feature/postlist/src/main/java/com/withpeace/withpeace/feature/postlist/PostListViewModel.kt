package com.withpeace.withpeace.feature.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.usecase.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
) : ViewModel() {

    private val _currentTopic = MutableStateFlow(PostTopic.FREEDOM)
    val currentTopic = _currentTopic.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val postListPagingFlow = currentTopic.flatMapLatest { postTopic ->
        fetchPostList(postTopic)
    }

    fun onTopicChanged(postTopic: PostTopic) {
        _currentTopic.update { postTopic }
    }

    private fun fetchPostList(postTopic: PostTopic): Flow<PagingData<Post>> {
        val pagingData = getPostsUseCase(
                postTopic = postTopic,
                pageSize = PAGE_SIZE,
            onError = { throw IllegalStateException() },
        )
        return Pager(
            config = pagingData.pagingConfig,
            pagingSourceFactory = { pagingData.pagingSource },
        ).flow.cachedIn(viewModelScope)
    }

    companion object {
        private const val PAGE_SIZE = 7
    }
}

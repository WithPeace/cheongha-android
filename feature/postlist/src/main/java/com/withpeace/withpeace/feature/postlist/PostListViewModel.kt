package com.withpeace.withpeace.feature.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.usecase.GetPostsUseCase
import com.withpeace.withpeace.core.ui.PostTopicUiState
import com.withpeace.withpeace.core.ui.post.PostUiModel
import com.withpeace.withpeace.core.ui.post.toPostUiModel
import com.withpeace.withpeace.core.ui.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
) : ViewModel() {
    private val _uiEvent = Channel<PostListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _currentTopic = MutableStateFlow(PostTopicUiState.FREE)
    val currentTopic = _currentTopic.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val postListPagingFlow =
        currentTopic.flatMapLatest { postTopic ->
            fetchPostList(postTopic.toDomain())
        }

    fun onTopicChanged(postTopic: PostTopicUiState) {
        _currentTopic.update { postTopic }
    }

    private fun fetchPostList(postTopic: PostTopic): Flow<PagingData<PostUiModel>> {
        val pagingData =
            getPostsUseCase(
                postTopic = postTopic,
                pageSize = PAGE_SIZE,
                onError = {
                    when (it) {
                        is WithPeaceError.GeneralError -> _uiEvent.send(PostListUiEvent.NetworkError)
                        is WithPeaceError.UnAuthorized -> _uiEvent.send(PostListUiEvent.UnAuthorizedError)
                    }
                    throw IllegalStateException() // LoadStateError를 내보내기 위함
                },
            )
        return Pager(
            config = pagingData.pagingConfig,
            pagingSourceFactory = { pagingData.pagingSource },
        ).flow.map {
            it.map { it.toPostUiModel() }
        }.cachedIn(viewModelScope)
    }

    companion object {
        private const val PAGE_SIZE = 7
    }
}

package com.withpeace.withpeace.feature.postlist

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.usecase.GetPostsUseCase
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel
import com.withpeace.withpeace.core.ui.post.PostUiModel
import com.withpeace.withpeace.core.ui.post.toDomain
import com.withpeace.withpeace.core.ui.post.toPostUiModel
import com.withpeace.withpeace.feature.postlist.navigation.POST_LIST_DELETED_POST_ID_ARGUMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
) : ViewModel() {
    private val _uiEvent = Channel<PostListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _currentTopic = MutableStateFlow(PostTopicUiModel.FREEDOM)
    val currentTopic = _currentTopic.asStateFlow()

    private val _postListPagingFlow = MutableStateFlow(PagingData.empty<PostUiModel>())
    private val deletedIdsFlow = MutableStateFlow(setOf<Long>())

    val postListPagingFlow = combine(_postListPagingFlow, deletedIdsFlow) { paging, deletedPostId ->
        paging.filter {
            deletedPostId.contains(it.postId).not()
        }
    }.cachedIn(viewModelScope)
    init {
        fetchPostList(currentTopic.value)
    }

    fun onTopicChanged(postTopic: PostTopicUiModel) {
        _currentTopic.update { postTopic }
        fetchPostList(postTopic)
    }

    private fun fetchPostList(postTopic: PostTopicUiModel) {
        viewModelScope.launch {
            _postListPagingFlow.update {
                getPostsUseCase(
                    postTopic = postTopic.toDomain(),
                    pageSize = PAGE_SIZE,
                    onError = {
                        when (it) {
                            ClientError.AuthExpired -> _uiEvent.send(PostListUiEvent.UnAuthorizedError)
                            else -> _uiEvent.send(PostListUiEvent.NetworkError)
                        }
                        throw IllegalStateException() // LoadStateError를 내보내기 위함
                    },
                ).map {
                    it.map { post -> post.toPostUiModel() }
                }.cachedIn(viewModelScope).firstOrNull() ?: PagingData.empty()
            }
        }
    }

    fun updateDeletedId(id: Long) {
        deletedIdsFlow.update { it + id }
    }

    companion object {
        private const val PAGE_SIZE = 7
    }
}

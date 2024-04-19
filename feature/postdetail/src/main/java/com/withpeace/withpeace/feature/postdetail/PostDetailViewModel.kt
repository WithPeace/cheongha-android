package com.withpeace.withpeace.feature.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.usecase.DeletePostUseCase
import com.withpeace.withpeace.core.domain.usecase.GetCurrentUserIdUseCase
import com.withpeace.withpeace.core.domain.usecase.GetPostDetailUseCase
import com.withpeace.withpeace.core.ui.post.toUiModel
import com.withpeace.withpeace.feature.postdetail.navigation.POST_DETAIL_ID_ARGUMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val deletePostUseCase: DeletePostUseCase,
) : ViewModel() {

    private val postId =
        checkNotNull(savedStateHandle.get<Long>(POST_DETAIL_ID_ARGUMENT)) { "게시글 아이디가 유효하지 않아요" }

    private val _postUiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val postUiState = _postUiState.asStateFlow()

    private val _postUiEvent = Channel<PostDetailUiEvent>()
    val postUiEvent = _postUiEvent.receiveAsFlow()

    init {
        fetchPostDetail()
    }

    private fun fetchPostDetail() {
        getPostDetailUseCase(
            postId,
            onError = {
                when(it) {
                    ResponseError.NOT_FOUND_RESOURCE -> _postUiState.update { PostDetailUiState.NotFound }
                    ClientError.AuthExpired -> _postUiState.update { PostDetailUiState.UnAuthorized }
                    else -> _postUiState.update { PostDetailUiState.FailByNetwork }
                }
                // TODO: 게시글 NotFound 에러 대응
            },
        ).onEach { data ->
            val currentUserId = getCurrentUserIdUseCase()
            _postUiState.update { PostDetailUiState.Success(data.toUiModel(currentUserId)) }
        }.onStart {
            _postUiState.update { PostDetailUiState.Loading }
        }.launchIn(viewModelScope)
    }

    fun deletePost() {
        deletePostUseCase(
            postId = postId,
            onError = {
                when (it) {
                    ClientError.AuthExpired -> _postUiEvent.send(PostDetailUiEvent.UnAuthorzied)
                    else -> _postUiEvent.send(PostDetailUiEvent.DeleteFailByNetworkError)
                }
            },
        ).onEach { _postUiEvent.send(PostDetailUiEvent.DeleteSuccess) }.launchIn(viewModelScope)
    }
}

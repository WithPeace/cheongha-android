package com.withpeace.withpeace.feature.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.usecase.DeletePostUseCase
import com.withpeace.withpeace.core.domain.usecase.GetCurrentUserIdUseCase
import com.withpeace.withpeace.core.domain.usecase.GetPostDetailUseCase
import com.withpeace.withpeace.core.domain.usecase.RegisterCommentUseCase
import com.withpeace.withpeace.core.domain.usecase.ReportPostUseCase
import com.withpeace.withpeace.core.ui.post.ReportTypeUiModel
import com.withpeace.withpeace.core.ui.post.toDomain
import com.withpeace.withpeace.core.ui.post.toUiModel
import com.withpeace.withpeace.feature.postdetail.navigation.POST_DETAIL_ID_ARGUMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val registerCommentUseCase: RegisterCommentUseCase,
    private val reportPostUseCase: ReportPostUseCase,
) : ViewModel() {

    private val postId =
        checkNotNull(savedStateHandle.get<Long>(POST_DETAIL_ID_ARGUMENT)) { "게시글 아이디가 유효하지 않아요" }

    private val _postUiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Init)
    val postUiState = _postUiState.asStateFlow()

    private val _commentText = MutableStateFlow("")
    val commentText = _commentText.asStateFlow()

    private val _postUiEvent = Channel<PostDetailUiEvent>()
    val postUiEvent = _postUiEvent.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchPostDetail()
    }

    private fun fetchPostDetail(
        onSuccess: suspend () -> Unit = {},
    ) {
        getPostDetailUseCase(
            postId,
            onError = {
                when(it) {
                    ResponseError.NOT_FOUND_RESOURCE -> _postUiState.update { PostDetailUiState.NotFound }
                    ClientError.AuthExpired -> _postUiEvent.send(PostDetailUiEvent.UnAuthorized)
                    else -> _postUiState.update { PostDetailUiState.FailByNetwork }
                }
            },
        ).onEach { data ->
            val currentUserId = getCurrentUserIdUseCase()
            _postUiState.update { PostDetailUiState.Success(data.toUiModel(currentUserId)) }
            onSuccess()
        }.onStart {
            _isLoading.update { true }
        }.onCompletion {
            _isLoading.update { false }
        }.launchIn(viewModelScope)
    }

    fun deletePost() {
        deletePostUseCase(
            postId = postId,
            onError = {
                when (it) {
                    ClientError.AuthExpired -> _postUiEvent.send(PostDetailUiEvent.UnAuthorized)
                    else -> _postUiEvent.send(PostDetailUiEvent.DeleteFailByNetworkError)
                }
            },
        ).onEach {
            _postUiEvent.send(PostDetailUiEvent.DeleteSuccess)
        }.launchIn(viewModelScope)
    }

    fun onCommentTextChanged(input: String) {
        viewModelScope.launch { _commentText.update { input } }
    }

    fun registerComment() {
        if (commentText.value == "") return
        registerCommentUseCase(
            postId = postId,
            content = commentText.value,
            onError = {
                when (it) {
                    ClientError.AuthExpired -> _postUiEvent.send(PostDetailUiEvent.UnAuthorized)
                    else -> _postUiEvent.send(PostDetailUiEvent.RegisterCommentFailByNetwork)
                }
            },
        ).onStart {
            _isLoading.update { true }
        }.onEach {
            fetchPostDetail {
                _commentText.update { "" }
                _postUiEvent.send(PostDetailUiEvent.RegisterCommentSuccess)
            }
        }.onCompletion {
            _isLoading.update { false }
        }.launchIn(viewModelScope)
    }

    fun reportPost(
        postId: Long,
        reportTypeUiModel: ReportTypeUiModel,
    ) {
        reportPostUseCase(
            postId, reportTypeUiModel.toDomain(),
            onError = {
                when (it) {
                    ResponseError.POST_DUPLICATED_ERROR -> _postUiEvent.send(PostDetailUiEvent.ReportPostDuplicated)
                    else -> _postUiEvent.send(PostDetailUiEvent.ReportPostFail)
                }
            },
        ).onStart {
            _isLoading.update { true }
        }.onEach {
            _postUiEvent.send(PostDetailUiEvent.ReportPostSuccess)
        }.onCompletion {
            _isLoading.update { false }
        }.launchIn(viewModelScope)
    }
}

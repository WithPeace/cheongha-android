package com.withpeace.withpeace.feature.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.GetPostDetailUseCase
import com.withpeace.withpeace.feature.postdetail.navigation.POST_DETAIL_ID_ARGUMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPostDetailUseCase: GetPostDetailUseCase,
) : ViewModel() {
    private val postId =
        checkNotNull(savedStateHandle.get<Long>(POST_DETAIL_ID_ARGUMENT)) { "게시글 아이디가 유효하지 않아요" }

    private val _postUiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val postUiState = _postUiState.asStateFlow()

    init {
        fetchPostDetail()
    }

    private fun fetchPostDetail() {
        getPostDetailUseCase(
            postId,
            onError = { _postUiState.update { PostDetailUiState.Fail } },
        ).onEach { data ->
            _postUiState.update { PostDetailUiState.Success(data) }
        }.onStart {
            _postUiState.update { PostDetailUiState.Loading }
        }.launchIn(viewModelScope)
    }
}

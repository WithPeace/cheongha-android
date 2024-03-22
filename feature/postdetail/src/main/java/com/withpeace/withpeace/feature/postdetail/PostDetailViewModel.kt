package com.withpeace.withpeace.feature.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.GetPostDetailUseCase
import com.withpeace.withpeace.feature.postdetail.navigation.POST_DETAIL_ID_ARGUMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPostDetailUseCase: GetPostDetailUseCase,
) : ViewModel() {
    private val postId =
        checkNotNull(savedStateHandle.get<Long>(POST_DETAIL_ID_ARGUMENT)) { "게시글 아이디가 유효하지 않아요" }

    val postUiState = flow {
        getPostDetailUseCase(
            postId,
            onError = { emit(PostDetailUiState.Fail) },
        ).onEach { emit(PostDetailUiState.Success(it)) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PostDetailUiState.Loading)
}

package com.withpeace.withpeace.feature.registerpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.Post
import com.withpeace.withpeace.core.domain.model.PostTopic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterPostViewModel @Inject constructor(

) : ViewModel() {

    private val _postUiState = MutableStateFlow(
        Post(
            title = "",
            content = "",
            topic = null,
            imageUrls = listOf("","","","",""),
        ),
    )
    val postUiState = _postUiState.asStateFlow()

    private val _uiEvent = Channel<RegisterPostUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _showTopicBottomSheet = MutableStateFlow(false)
    val showBottomSheet = _showTopicBottomSheet.asStateFlow()

    fun onTitleChanged(inputTitle: String) {
        _postUiState.update { it.copy(title = inputTitle) }
    }

    fun onContentChanged(inputContent: String) {
        _postUiState.update { it.copy(content = inputContent) }
    }

    fun onTopicChanged(inputTopic: PostTopic) {
        _postUiState.update { it.copy(topic = inputTopic) }
    }

    fun onImageUrlsChanged(imageUrls: List<String>) {
        _postUiState.update { it.copy(imageUrls = imageUrls) }
    }

    fun onImageUrlDeleted(deletedImageUrl: String) {
        val newUrls = postUiState.value.imageUrls.filter { it != deletedImageUrl }
        _postUiState.update { it.copy(imageUrls = newUrls) }
    }

    fun onShowBottomSheetChanged(input: Boolean) {
        _showTopicBottomSheet.update { input }
    }

    fun onRegisterPostCompleted() {
        val post = postUiState.value
        viewModelScope.launch {
            when {
                post.title.isBlank() -> _uiEvent.send(RegisterPostUiEvent.TitleBlank)
                post.content.isBlank() -> _uiEvent.send(RegisterPostUiEvent.ContentBlank)
                post.topic == null -> _uiEvent.send(RegisterPostUiEvent.TopicBlank)
                else -> {
                    _uiEvent.send(RegisterPostUiEvent.PostSuccess)
                }
            }
        }
    }
}

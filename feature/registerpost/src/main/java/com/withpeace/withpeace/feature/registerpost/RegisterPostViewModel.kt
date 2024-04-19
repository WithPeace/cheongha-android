package com.withpeace.withpeace.feature.registerpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.image.LimitedImages
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import com.withpeace.withpeace.core.domain.usecase.RegisterPostUseCase
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel
import com.withpeace.withpeace.core.ui.post.RegisterPostUiModel
import com.withpeace.withpeace.core.ui.post.toDomain
import com.withpeace.withpeace.core.ui.post.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterPostViewModel @Inject constructor(
    private val registerPostUseCase: RegisterPostUseCase,
) : ViewModel() {

    private var isUpdate: Boolean = false

    private val registerPost = MutableStateFlow(
        RegisterPost(
            id = null,
            title = "",
            content = "",
            topic = null,
            images = LimitedImages(emptyList()),
        ),
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val registerPostUiModel = registerPost.map { it.toUi() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            RegisterPostUiModel(),
        )

    private val _uiEvent = Channel<RegisterPostUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet = _showBottomSheet.asStateFlow()

    fun onTitleChanged(inputTitle: String) {
        registerPost.update { it.copy(title = inputTitle) }
    }

    fun onContentChanged(inputContent: String) {
        registerPost.update { it.copy(content = inputContent) }
    }

    fun onTopicChanged(inputTopic: PostTopicUiModel) {
        registerPost.update { it.copy(topic = inputTopic.toDomain()) }
    }

    fun onImageUrlsAdded(imageUrls: List<String>) {
        registerPost.update { it.copy(images = it.images.addImages(imageUrls)) }
    }

    fun onImageUrlDeleted(index: Int) {
        registerPost.update { it.copy(images = it.images.deleteImage(index)) }
    }

    fun onShowBottomSheetChanged(input: Boolean) {
        _showBottomSheet.update { input }
    }

    fun onRegisterPostCompleted() {
        val registerPostValue = registerPost.value
        viewModelScope.launch {
            when {
                registerPostValue.title.isBlank() -> _uiEvent.send(RegisterPostUiEvent.TitleBlank)
                registerPostValue.content.isBlank() -> _uiEvent.send(RegisterPostUiEvent.ContentBlank)
                registerPostValue.topic == null -> _uiEvent.send(RegisterPostUiEvent.TopicBlank)
                else -> registerPostUseCase(post = registerPostValue) {
                    _uiEvent.send(RegisterPostUiEvent.RegisterFail(it))
                }.onStart {
                    _isLoading.update { true }
                    delay(5000L)
                }.onCompletion {
                    _isLoading.update { false }
                }.collect { postId ->
                    _uiEvent.send(RegisterPostUiEvent.RegisterSuccess(postId))
                }
            }
        }
    }

    fun initRegisterPost(originPost: RegisterPostUiModel?) {
        if (!isUpdate) {
            originPost?.let { registerPostUiModel ->
                registerPost.update { registerPostUiModel.toDomain() }
            }
            isUpdate = true
        }
    }

    companion object {
        const val IMAGE_MAX_SIZE = 5
    }
}

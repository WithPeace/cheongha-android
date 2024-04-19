package com.withpeace.withpeace.feature.registerpost

import com.withpeace.withpeace.core.domain.model.error.CheonghaError

sealed interface RegisterPostUiEvent {
    data object TitleBlank : RegisterPostUiEvent
    data object ContentBlank : RegisterPostUiEvent
    data object TopicBlank : RegisterPostUiEvent
    data class RegisterSuccess(val postId: Long) : RegisterPostUiEvent
    data class RegisterFail(val error: CheonghaError) : RegisterPostUiEvent
}

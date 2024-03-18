package com.withpeace.withpeace.feature.registerpost

import com.withpeace.withpeace.core.domain.model.WithPeaceError

sealed interface RegisterPostUiEvent {
    data object TitleBlank : RegisterPostUiEvent
    data object ContentBlank : RegisterPostUiEvent
    data object TopicBlank : RegisterPostUiEvent
    data object PostSuccess: RegisterPostUiEvent
    data class  PostFail(val error:WithPeaceError):RegisterPostUiEvent
}

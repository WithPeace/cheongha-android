package com.withpeace.withpeace.feature.registerpost

sealed interface RegisterPostUiEvent {
    data object TitleBlank : RegisterPostUiEvent
    data object ContentBlank : RegisterPostUiEvent
    data object TopicBlank : RegisterPostUiEvent
    data object PostSuccess: RegisterPostUiEvent
}

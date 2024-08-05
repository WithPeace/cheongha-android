package com.withpeace.withpeace.feature.disablepolicy

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.BookmarkPolicyUseCase
import com.withpeace.withpeace.feature.disablepolicy.navigation.DISABLE_POLICY_ID_ARGUMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisablePolicyViewModel @Inject constructor(
    private val bookmarkPolicyUseCase: BookmarkPolicyUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val policyId = savedStateHandle.get<String>(DISABLE_POLICY_ID_ARGUMENT)
        ?: throw IllegalStateException("ID를 받아올 수 없어요.")
    private val _uiEvent = Channel<DisabledPolicyUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun unBookmark() {
        viewModelScope.launch {
            bookmarkPolicyUseCase(
                policyId = policyId, isBookmarked = false,
                onError = {
                    _uiEvent.send(DisabledPolicyUiEvent.UnBookmarkFailure)
                },
            ).collect {
                _uiEvent.send(DisabledPolicyUiEvent.UnBookmarkSuccess(policyId))
            }
        }
    }
}

sealed interface DisabledPolicyUiEvent {
    data class UnBookmarkSuccess(val policyId: String) : DisabledPolicyUiEvent
    data object UnBookmarkFailure : DisabledPolicyUiEvent
}
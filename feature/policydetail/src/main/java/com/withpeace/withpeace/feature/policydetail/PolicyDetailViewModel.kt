package com.withpeace.withpeace.feature.policydetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.BookmarkPolicyUseCase
import com.withpeace.withpeace.core.domain.usecase.GetYouthPolicyDetailUseCase
import com.withpeace.withpeace.feature.policydetail.navigation.POLICY_DETAIL_YOUTH_POLICY_ID_ARGUMENT
import com.withpeace.withpeace.feature.policydetail.uistate.YouthPolicyDetailUiEvent
import com.withpeace.withpeace.feature.policydetail.uistate.YouthPolicyDetailUiState
import com.withpeace.withpeace.feature.policydetail.uistate.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class PolicyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPolicyDetailUseCase: GetYouthPolicyDetailUseCase,
    private val bookmarkUseCase: BookmarkPolicyUseCase,
) : ViewModel() {
    private val policyId = savedStateHandle.get<String>(POLICY_DETAIL_YOUTH_POLICY_ID_ARGUMENT)
        ?: throw IllegalArgumentException("Id를 불러올 수 없습니다.")

    private val _policyDetailUiState: MutableStateFlow<YouthPolicyDetailUiState> =
        MutableStateFlow(YouthPolicyDetailUiState.Loading)
    val policyDetailUiState = _policyDetailUiState.asStateFlow()

    private val _uiEvent = Channel<YouthPolicyDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val debounceFlow = MutableSharedFlow<Boolean>()

    private var lastBookmarkSuccessState = false

    init {
        fetchPolicyDetail(policyId)

        viewModelScope.launch {
            debounceFlow.debounce(300L).collectLatest {
                bookmarkUseCase(
                    policyId = policyId, isBookmarked = it,
                    onError = {
                        _policyDetailUiState.update { state ->
                            (state as? YouthPolicyDetailUiState.Success)?.let { successState ->
                                successState.copy(
                                    youthPolicyDetail = successState.youthPolicyDetail.copy(
                                        isBookmarked = lastBookmarkSuccessState,
                                    ),
                                )
                            } ?: state
                        }
                    },
                ).collect { result ->
                    if (result.isBookmarked) {
                        _uiEvent.send(YouthPolicyDetailUiEvent.BookmarkSuccess)
                    } else {
                        _uiEvent.send(YouthPolicyDetailUiEvent.UnBookmarkSuccess)
                    }
                    lastBookmarkSuccessState = result.isBookmarked
                }
            }
        }

    }

    private fun fetchPolicyDetail(policyId: String) {
        viewModelScope.launch {
            getPolicyDetailUseCase(
                policyId = policyId,
                onError = {

                },
            ).collect { data ->
                lastBookmarkSuccessState = data.isBookmarked

                _policyDetailUiState.update { YouthPolicyDetailUiState.Success(data.toUiModel()) }
            }
        }
    }

    fun bookmark(isBookmarked: Boolean) {
        _policyDetailUiState.update { state ->
            (state as? YouthPolicyDetailUiState.Success)?.let { successState ->
                successState.copy(
                    youthPolicyDetail = successState.youthPolicyDetail.copy(isBookmarked = isBookmarked),
                )
            } ?: state
        }

        viewModelScope.launch {
            debounceFlow.emit(isBookmarked)
        }
    }
}
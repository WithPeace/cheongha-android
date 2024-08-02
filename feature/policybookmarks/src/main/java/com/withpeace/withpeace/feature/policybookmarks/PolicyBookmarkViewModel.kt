package com.withpeace.withpeace.feature.policybookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.extension.groupBy
import com.withpeace.withpeace.core.domain.model.policy.BookmarkInfo
import com.withpeace.withpeace.core.domain.usecase.BookmarkPolicyUseCase
import com.withpeace.withpeace.core.domain.usecase.GetBookmarkedPolicyUseCase
import com.withpeace.withpeace.feature.policybookmarks.uistate.BookmarkedPolicyUIState
import com.withpeace.withpeace.feature.policybookmarks.uistate.PolicyBookmarkUiEvent
import com.withpeace.withpeace.feature.policybookmarks.uistate.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class PolicyBookmarkViewModel @Inject constructor(
    private val getBookmarkedPolicyUseCase: GetBookmarkedPolicyUseCase,
    private val bookmarkPolicyUseCase: BookmarkPolicyUseCase,
) : ViewModel() {
    private val _bookmarkedPolicies: MutableStateFlow<BookmarkedPolicyUIState> =
        MutableStateFlow(BookmarkedPolicyUIState.Loading)

    val bookmarkedPolicies = _bookmarkedPolicies.asStateFlow()

    private val _uiEvent = Channel<PolicyBookmarkUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val lastSuccessByWhetherOfBookmarks = mutableMapOf<String, Boolean>()


    private val debounceFlow =
        MutableSharedFlow<BookmarkInfo>()

    init {
        viewModelScope.launch {
            debounceFlow.groupBy { it.id }
                .flatMapMerge { data ->
                    data.second.debounce(300L)
                }.collect { bookmarkInfo ->
                    bookmarkPolicyUseCase(
                        bookmarkInfo.id, bookmarkInfo.isBookmarked,
                        onError = {
                            _bookmarkedPolicies.update { state ->
                                (state as? BookmarkedPolicyUIState.Success)?.let { successState ->
                                    successState.copy(
                                        youthPolicies = successState.youthPolicies.map { policy ->
                                            policy.takeIf { it.id == bookmarkInfo.id }
                                                ?.copy(isBookmarked = lastSuccessByWhetherOfBookmarks[policy.id]?: !policy.isBookmarked)
                                                ?: policy
                                        },
                                    )
                                } ?: state
                            }
                            _uiEvent.send(PolicyBookmarkUiEvent.BookmarkFailure)
                        },
                    ).collect {
                        lastSuccessByWhetherOfBookmarks[it.id] = it.isBookmarked
                        if (it.isBookmarked) {
                            _uiEvent.send(PolicyBookmarkUiEvent.BookmarkSuccess)
                        } else {
                            _uiEvent.send(PolicyBookmarkUiEvent.UnBookmarkSuccess)
                        }
                    }
                }
        }
        getBookmarkedPolicies()
    }

    private fun getBookmarkedPolicies() {
        viewModelScope.launch {
            getBookmarkedPolicyUseCase(
                onError = {
                    _bookmarkedPolicies.update { BookmarkedPolicyUIState.Failure }
                },
            ).collect { bookmarkedPolicies ->
                _bookmarkedPolicies.update {
                    BookmarkedPolicyUIState.Success(bookmarkedPolicies.map { it.toUiModel() })
                }
                bookmarkedPolicies.forEach {
                    lastSuccessByWhetherOfBookmarks[it.id] = it.isBookmarked
                }
            }
        }
    }

    fun bookmark(id: String, isChecked: Boolean) {
        _bookmarkedPolicies.update { state ->
            (state as? BookmarkedPolicyUIState.Success)?.let { successState ->
                successState.copy(
                    youthPolicies = successState.youthPolicies.map { policy ->
                        policy.takeIf { it.id == id }?.copy(isBookmarked = isChecked) ?: policy
                    },
                )
            } ?: state
        }
        viewModelScope.launch {
            debounceFlow.emit(BookmarkInfo(id, isChecked))
        }
    }
}

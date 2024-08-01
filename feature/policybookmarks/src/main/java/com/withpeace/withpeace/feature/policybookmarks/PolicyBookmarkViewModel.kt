package com.withpeace.withpeace.feature.policybookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.BookmarkPolicyUseCase
import com.withpeace.withpeace.core.domain.usecase.GetBookmarkedPolicyUseCase
import com.withpeace.withpeace.feature.policybookmarks.uistate.BookmarkedPolicyUIState
import com.withpeace.withpeace.feature.policybookmarks.uistate.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PolicyBookmarkViewModel @Inject constructor(
    private val getBookmarkedPolicyUseCase: GetBookmarkedPolicyUseCase,
    private val bookmarkPolicyUseCase: BookmarkPolicyUseCase,
) : ViewModel() {
    private val _bookmarkedPolicies: MutableStateFlow<BookmarkedPolicyUIState> =
        MutableStateFlow(BookmarkedPolicyUIState.Loading)

    val bookmarkedPolicies = _bookmarkedPolicies.asStateFlow()

    private val debounceFlow =
        MutableSharedFlow<BookmarkInfo>()

    init {
        viewModelScope.launch {
            debounceFlow.groupBy { it.id }
                .flatMapMerge {
                    it.second.debounceFlow(300L)
                }.collect { bookmarkInfo ->
                    bookmarkPolicyUseCase(
                        bookmarkInfo.id, bookmarkInfo.isBookmarked,
                        onError = {
                            _bookmarkedPolicies.update { state ->
                                (state as? BookmarkedPolicyUIState.Success)?.let { successState ->
                                    successState.copy(
                                        youthPolicies = successState.youthPolicies.map { policy ->
                                            policy.takeIf { it.id == bookmarkInfo.id }
                                                ?.copy(isBookmarked = bookmarkInfo.isBookmarked)
                                                ?: policy
                                        },
                                    )
                                } ?: state
                            }
                            // 실패 스낵바 표시
                        },
                    ).collect() // 성공 스낵바 표시
                }
        }
        getBookmarkedPolicies()
    }

    fun getBookmarkedPolicies() {
        viewModelScope.launch {
            getBookmarkedPolicyUseCase(
                onError = {
                    _bookmarkedPolicies.update { BookmarkedPolicyUIState.Failure }
                },
            ).collect { bookmarkedPolicies ->
                _bookmarkedPolicies.update {
                    BookmarkedPolicyUIState.Success(bookmarkedPolicies.map { it.toUiModel() })
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

data class BookmarkInfo(
    val id: String,
    val isBookmarked: Boolean,
)

fun <T> Flow<T>.debounceFlow(timeoutMillis: Long): Flow<T> = debounce(timeoutMillis)

fun <T, K> Flow<T>.groupBy(getKey: (T) -> K): Flow<Pair<K, Flow<T>>> = flow {
    val storage = mutableMapOf<K, SendChannel<T>>()
    try {
        collect { t ->
            val key = getKey(t)
            storage.getOrPut(key) {
                Channel<T>(32).also { emit(key to it.consumeAsFlow()) }
            }.send(t)
        }
    } finally {
        storage.values.forEach { chan -> chan.close() }
    }
}

// https://stackoverflow.com/questions/43431501/rx-java-split-infinite-stream-into-groups-and-debounce-each-group-separate
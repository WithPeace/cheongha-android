@file:OptIn(ExperimentalCoroutinesApi::class)

package com.withpeace.withpeace.feature.policybookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.BookmarkPolicyUseCase
import com.withpeace.withpeace.core.domain.usecase.GetBookmarkedPolicyUseCase
import com.withpeace.withpeace.core.domain.usecase.UnBookmarkPolicyUseCase
import com.withpeace.withpeace.feature.policybookmarks.uistate.BookmarkedPolicyUIState
import com.withpeace.withpeace.feature.policybookmarks.uistate.toUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
class PolicyBookmarkViewModel @Inject constructor(
    private val getBookmarkedPolicyUseCase: GetBookmarkedPolicyUseCase,
    private val bookmarkPolicyUseCase: BookmarkPolicyUseCase,
    private val unBookmarkPolicyUseCase: UnBookmarkPolicyUseCase,
) : ViewModel() {
    private val _bookmarkedPolicies: MutableStateFlow<BookmarkedPolicyUIState> =
        MutableStateFlow(BookmarkedPolicyUIState.Loading)

    private val debounceFlow =
        MutableSharedFlow<BookmarkInfo>()

    init {
        viewModelScope.launch {
            debounceFlow.map {
                Pair(it.id, it.isBookmarked)
            }.toMap()
            // debounce 걸리면 어차피 다 필터됨 걸린거임
        }
        getBookmarkedPolicies()
    }

    suspend fun <K, V> Flow<Pair<K, V>>.toMap(): Map<K, V> {
        val map = mutableMapOf<K, V>()
        collect { (k, v) ->
            map[k] = v
        }
        return map
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

    fun bookmark(isChecked: Boolean, id: String) {
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
        // _toggleEvents[id]?.update { isChecked }
    }

    fun createDebouncedFlow(id: String, debounceTimeMs: Long): Flow<BookmarkInfo> {
        return debounceFlow
            .filter { it.id == id }
            .debounce(debounceTimeMs)
    }


}

data class BookmarkInfo(
    val id: String,
    val isBookmarked: Boolean,
)

fun <T> Flow<T>.debounceFlow(timeoutMillis: Long): Flow<T> = debounce(timeoutMillis)

suspend fun main() {
    val source: Flow<BookmarkInfo> = flow {
        emit(BookmarkInfo("0", false))
        emit(BookmarkInfo("0", false))
        emit(BookmarkInfo("1", true))
        emit(BookmarkInfo("0", true))
        delay(400L)
        emit(BookmarkInfo("0", false))

    }

    source.groupBy { it.id }
        .flatMapMerge { it.second.debounceFlow(300L) }
        .collect { value ->
            println(value.id + " is " + value.isBookmarked)
        }


}
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
// 실제 시나리오. 같은 ID에 대해서 300L에 들어온 값은 debounce API 요청
// 좀 더 정확히는 같은 아이디
// Map<Id, Flow<>> 방식도 있긴 할듯
// Flow<Map<Id, Boolean>> 는 어떨까?

// https://stackoverflow.com/questions/43431501/rx-java-split-infinite-stream-into-groups-and-debounce-each-group-separate
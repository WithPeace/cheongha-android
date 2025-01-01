package com.withpeace.withpeace.feature.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.withpeace.withpeace.core.domain.extension.groupBy
import com.withpeace.withpeace.core.domain.model.error.SingleCharacterSearchException
import com.withpeace.withpeace.core.domain.model.policy.BookmarkInfo
import com.withpeace.withpeace.core.domain.model.search.SearchKeyword
import com.withpeace.withpeace.core.domain.usecase.AddKeywordUseCase
import com.withpeace.withpeace.core.domain.usecase.BookmarkPolicyUseCase
import com.withpeace.withpeace.core.domain.usecase.ClearAllKeywordsUseCase
import com.withpeace.withpeace.core.domain.usecase.GetAllKeywordsUseCase
import com.withpeace.withpeace.core.domain.usecase.SearchUseCase
import com.withpeace.withpeace.core.ui.policy.YouthPolicyUiModel
import com.withpeace.withpeace.core.ui.policy.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val addKeywordUseCase: AddKeywordUseCase,
    private val getAllKeywordsUseCase: GetAllKeywordsUseCase,
    private val bookmarkPolicyUseCase: BookmarkPolicyUseCase,
    private val clearAllKeywordsUseCase: ClearAllKeywordsUseCase,
) : ViewModel() {

    private val _uiEvent: Channel<SearchUiEvent> = Channel()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState: MutableStateFlow<SearchUiState> =
        MutableStateFlow(SearchUiState.Initialized)
    val uiState = _uiState.asStateFlow()

    private val bookmarkStateFlow =
        MutableStateFlow(mapOf<String, Boolean>())

    private val debounceFlow = MutableSharedFlow<BookmarkInfo>(replay = 1)

    private val lastByWhetherSuccessOfBookmarks =
        mutableMapOf<String, Boolean>() // optimistic UI에서 실패시에 사용할 캐시 데이터

    private val _youthPolicyPagingFlow = MutableStateFlow(PagingData.empty<YouthPolicyUiModel>())
    val youthPolicyPagingFlow =
        combine(
            _youthPolicyPagingFlow.asStateFlow(),
            bookmarkStateFlow,
        ) { youthPolicyPagingFlow, bookmarkFlow ->
            youthPolicyPagingFlow.map {
                lastByWhetherSuccessOfBookmarks[it.id] = it.isBookmarked
                val bookmarkState = bookmarkFlow[it.id]
                it.copy(isBookmarked = bookmarkState ?: it.isBookmarked)
            }
        }.cachedIn(viewModelScope)

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword = _searchKeyword.asStateFlow()

    private val _recentSearchKeywords = MutableStateFlow<List<String>>(listOf())
    val recentSearchKeywords = _recentSearchKeywords.asStateFlow()

    private val _totalCountFlow = MutableStateFlow(0)
    val totalCountFlow = _totalCountFlow.asStateFlow() // Paging3에서 데이터를 같이 가져오는 것이 어려워 해당 프로퍼티로 구현

    init {
        viewModelScope.launch {
            _recentSearchKeywords.value = getAllKeywordsUseCase().map { it.value }
            debounceFlow.groupBy { it.id }.flatMapMerge {
                it.second.debounce(300L)
            }.collectLatest { bookmarkInfo -> // policyBookmarkViewModel과 다른 이유를 찾아보기
                bookmarkPolicyUseCase(
                    bookmarkInfo.id, bookmarkInfo.isBookmarked,
                    onError = {
                        bookmarkStateFlow.update {
                            it + mapOf(
                                bookmarkInfo.id to (lastByWhetherSuccessOfBookmarks[bookmarkInfo.id]
                                    ?: !bookmarkInfo.isBookmarked),
                            )
                        }
                        _uiEvent.send(SearchUiEvent.BookmarkFailure)
                    },
                ).collect { result ->
                    lastByWhetherSuccessOfBookmarks[result.id] = result.isBookmarked
                    if (result.isBookmarked) {
                        _uiEvent.send(SearchUiEvent.BookmarkSuccess)
                    } else {
                        _uiEvent.send(SearchUiEvent.UnBookmarkSuccess)
                    }
                }
            }
        }
    }

    fun onChangedKeyword(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun search() {
        viewModelScope.launch {
            runCatching {
                _youthPolicyPagingFlow.update {
                    searchUseCase(
                        keyword = searchKeyword.value,
                        onError = {
                        },
                        onReceiveTotalCount = {
                            _totalCountFlow.value = it
                        },
                    ).onStart {
                        _uiState.update {
                            SearchUiState.PagingData
                        }
                    }.map { data ->
                        data.map {
                            it.second.toUiModel()
                        }
                    }.cachedIn(viewModelScope).firstOrNull() ?: PagingData.empty()
                }
            }.onFailure {
                when (it) {
                    is SingleCharacterSearchException -> {
                        _uiEvent.send(SearchUiEvent.SingleCharacteristicError)
                    }
                }
                return@launch
            }
            addKeywordUseCase(SearchKeyword(searchKeyword.value))
        }
    }

    fun bookmark(id: String, isChecked: Boolean) {
        bookmarkStateFlow.update { it + mapOf(id to isChecked) }
        viewModelScope.launch {
            debounceFlow.emit(BookmarkInfo(id, isChecked))
        }
    }

    fun onClickSearchKeyword(keyword: String) {
        this._searchKeyword.value = keyword
        search()
    }

    fun removeKeywords() {
        viewModelScope.launch { clearAllKeywordsUseCase() }
        _recentSearchKeywords.value = listOf()
    }
}

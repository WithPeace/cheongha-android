package com.withpeace.withpeace.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.withpeace.withpeace.core.domain.extension.groupBy
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.policy.BookmarkInfo
import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.domain.usecase.BookmarkPolicyUseCase
import com.withpeace.withpeace.core.domain.usecase.GetYouthPoliciesUseCase
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.core.ui.policy.toDomain
import com.withpeace.withpeace.feature.home.uistate.HomeUiEvent
import com.withpeace.withpeace.feature.home.uistate.PolicyFiltersUiModel
import com.withpeace.withpeace.feature.home.uistate.YouthPolicyUiModel
import com.withpeace.withpeace.feature.home.uistate.toDomain
import com.withpeace.withpeace.feature.home.uistate.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val youthPoliciesUseCase: GetYouthPoliciesUseCase,
    private val bookmarkPolicyUseCase: BookmarkPolicyUseCase,
) : ViewModel() {
    private val bookmarkStateFlow = MutableStateFlow(mapOf<String, Boolean>())

    private val _youthPolicyPagingFlow = MutableStateFlow(PagingData.empty<YouthPolicyUiModel>())
    val youthPolicyPagingFlow =
        combine(
            _youthPolicyPagingFlow.asStateFlow(),
            bookmarkStateFlow,
        ) { youthPolicyPagingFlow, bookmarkFlow ->
            youthPolicyPagingFlow.map {
                val bookmarkState = bookmarkFlow[it.id]
                it.copy(isBookmarked = bookmarkState ?: it.isBookmarked)
            }
        }.cachedIn(viewModelScope)

    private val _selectingFilters = MutableStateFlow(PolicyFilters())
    val selectingFilters: StateFlow<PolicyFiltersUiModel> =
        _selectingFilters.map { it.toUiModel() }.stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(),
            PolicyFiltersUiModel(),
        )

    private val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val debounceFlow = MutableSharedFlow<BookmarkInfo>(replay = 1)

    private val lastByWhetherSuccessOfBookmarks =
        mutableMapOf<String, Boolean>() // optimistic UI에서 실패시에 사용할 캐시 데이터

    private var completedFilters = PolicyFilters()

    init {
        viewModelScope.launch {
            debounceFlow.groupBy { it.id }.flatMapMerge {
                it.second.debounce(300L)
            }.collectLatest {
                Log.d("testLog", "${it.id} + ${it.isBookmarked}")
                val result = bookmarkPolicyUseCase(
                    it.id, it.isBookmarked,
                    onError = {
                        Log.d("test","test12")
                        // _bookmarkedPolicies.update { state ->
                        //     (state as? BookmarkedPolicyUIState.Success)?.let { successState ->
                        //         successState.copy(
                        //             youthPolicies = successState.youthPolicies.map { policy ->
                        //                 policy.takeIf { it.id == bookmarkInfo.id }
                        //                     ?.copy(
                        //                         isBookmarked = lastByWhetherSuccessOfBookmarks[policy.id]
                        //                             ?: !policy.isBookmarked
                        //                     )
                        //                     ?: policy
                        //             },
                        //         )
                        //     } ?: state
                        // }
                        _uiEvent.send(HomeUiEvent.BookmarkFailure)
                    },
                ).first()

                lastByWhetherSuccessOfBookmarks[result.id] = result.isBookmarked
                if (result.isBookmarked) {
                    _uiEvent.send(HomeUiEvent.BookmarkSuccess)
                } else {
                    _uiEvent.send(HomeUiEvent.UnBookmarkSuccess)
                }
            }
        }
        fetchData()
    }


    private fun fetchData() {
        viewModelScope.launch {
            _youthPolicyPagingFlow.update {
                youthPoliciesUseCase(
                    filterInfo = completedFilters,
                    onError = {
                        when (it) {
                            ResponseError.EXPIRED_TOKEN_ERROR -> {}
                            else -> {}
                        }
                    },
                ).map {
                    it.map { youthPolicy ->
                        youthPolicy.toUiModel()
                    }
                }.cachedIn(viewModelScope).firstOrNull() ?: PagingData.empty()
            }
        }
    }

    fun onCheckClassification(classification: ClassificationUiModel) {
        _selectingFilters.update {
            it.updateClassification(classification.toDomain())
        }
    }

    fun onCheckRegion(region: RegionUiModel) {
        _selectingFilters.update {
            it.updateRegion(region.toDomain())
        }
    }

    fun onCompleteFilter() {
        completedFilters = selectingFilters.value.toDomain()
        fetchData()
    }

    fun onCancelFilter() {
        _selectingFilters.update { completedFilters }
    }

    fun onFilterAllOff() {
        _selectingFilters.update {
            it.removeAll()
        }
    }

    fun bookmark(id: String, isChecked: Boolean) {
        bookmarkStateFlow.update { it + mapOf(id to isChecked) }
        viewModelScope.launch {
            debounceFlow.emit(BookmarkInfo(id, isChecked))
        }
    }
}

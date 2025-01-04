package com.withpeace.withpeace.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.domain.usecase.BalanceGameVisitedUseCase
import com.withpeace.withpeace.core.domain.usecase.GetHotPoliciesUseCase
import com.withpeace.withpeace.core.domain.usecase.GetPolicyFilterUseCase
import com.withpeace.withpeace.core.domain.usecase.GetRecentPostUseCase
import com.withpeace.withpeace.core.domain.usecase.GetRecommendPoliciesUseCase
import com.withpeace.withpeace.core.domain.usecase.UpdateBalanceGameVisitedStatus
import com.withpeace.withpeace.core.domain.usecase.UpdatePolicyFilterUseCase
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.core.ui.policy.filtersetting.PolicyFiltersUiModel
import com.withpeace.withpeace.core.ui.policy.filtersetting.toDomain
import com.withpeace.withpeace.core.ui.policy.filtersetting.toUiModel
import com.withpeace.withpeace.core.ui.policy.toDomain
import com.withpeace.withpeace.feature.home.uistate.HotPolicyUiState
import com.withpeace.withpeace.feature.home.uistate.RecentPostsUiState
import com.withpeace.withpeace.feature.home.uistate.RecommendPolicyUiState
import com.withpeace.withpeace.feature.home.uistate.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecentPostUseCase: GetRecentPostUseCase,
    private val getRecommendPoliciesUseCase: GetRecommendPoliciesUseCase,
    private val getHotPoliciesUseCase: GetHotPoliciesUseCase,
    private val getPolicyFilterUseCase: GetPolicyFilterUseCase,
    private val updatePolicyFilterUseCase: UpdatePolicyFilterUseCase,
    private val updateBalanceGameVisitedStatus: UpdateBalanceGameVisitedStatus,
    balanceGameVisitedUseCase: BalanceGameVisitedUseCase,

) : ViewModel() {
    private val _recentPostsUiState: MutableStateFlow<RecentPostsUiState> =
        MutableStateFlow(RecentPostsUiState.Loading)
    val recentPostsUiState = _recentPostsUiState.asStateFlow()

    private val _recommendPolicyUiState: MutableStateFlow<RecommendPolicyUiState> =
        MutableStateFlow(RecommendPolicyUiState.Loading)
    val recommendPolicyUiState = _recommendPolicyUiState.asStateFlow()

    private val _hotPolicyUiState: MutableStateFlow<HotPolicyUiState> =
        MutableStateFlow(HotPolicyUiState.Loading)
    val hotPolicyUiState = _hotPolicyUiState.asStateFlow()

    private val _selectingFilters = MutableStateFlow(PolicyFilters())
    val selectingFilters: StateFlow<PolicyFiltersUiModel> =
        _selectingFilters.map { it.toUiModel() }.stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(),
            PolicyFiltersUiModel(),
        )

    private val _completedFilters = MutableStateFlow(PolicyFilters())
    val completedFilters: StateFlow<PolicyFiltersUiModel> =
        _completedFilters.map { it.toUiModel() }.stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(),
            PolicyFiltersUiModel(),
        )

    val isBalanceGameVisited = balanceGameVisitedUseCase()


    init {
        viewModelScope.launch {
            launch {
                getRecentPostUseCase(
                    onError = {

                    },
                ).collect { data ->
                    _recentPostsUiState.update {
                        RecentPostsUiState.Success(
                            data.map { it.toUiModel() },
                        )
                    }
                }
            }
            getRecommendPolicy()
            getHotPolicy()
            launch {
                getPolicyFilterUseCase(
                    onError = {
                    },
                ).collect { data ->
                    _selectingFilters.update { data }
                    _completedFilters.update { data }
                }
            }
        }
    }

    private fun CoroutineScope.getHotPolicy() {
        launch {
            getHotPoliciesUseCase(
                onError = {
                    _hotPolicyUiState.update {
                        HotPolicyUiState.Failure
                    }
                },
            ).collect { data ->
                _hotPolicyUiState.update {
                    HotPolicyUiState.Success(data.map { it.toUiModel() })
                }
            }
        }
    }

    private fun CoroutineScope.getRecommendPolicy() {
        launch {
            getRecommendPoliciesUseCase(
                onError = {
                    _recommendPolicyUiState.update {
                        RecommendPolicyUiState.Failure
                    }
                },
            ).collect { data ->
                _recommendPolicyUiState.update {
                    RecommendPolicyUiState.Success(data.map { it.toUiModel() })
                }
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
        viewModelScope.launch {
            updatePolicyFilterUseCase(
                policyFilters = selectingFilters.value.toDomain(),
                onError = {},
            ).collect {
                _completedFilters.update { selectingFilters.value.toDomain() }
                this.launch { getHotPolicy() }
                this.launch { getRecommendPolicy() }
            }
        }
    }

    fun onCancelFilter() {
        _selectingFilters.update { completedFilters.value.toDomain() }
    }

    fun onFilterAllOff() {
        _selectingFilters.update {
            it.removeAll()
        }
    }

    fun visitBalanceGame() {
        viewModelScope.launch {
            updateBalanceGameVisitedStatus(true)
        }
    }
}

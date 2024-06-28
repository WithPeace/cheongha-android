package com.withpeace.withpeace.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.domain.usecase.GetYouthPoliciesUseCase
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.core.ui.policy.toDomain
import com.withpeace.withpeace.feature.home.uistate.PolicyFiltersUiModel
import com.withpeace.withpeace.core.ui.policy.YouthPolicyUiModel
import com.withpeace.withpeace.core.ui.policy.toUiModel
import com.withpeace.withpeace.feature.home.uistate.toDomain
import com.withpeace.withpeace.feature.home.uistate.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val youthPoliciesUseCase: GetYouthPoliciesUseCase,
) : ViewModel() {
    private val _youthPolicyPagingFlow = MutableStateFlow(PagingData.empty<YouthPolicyUiModel>())
    val youthPolicyPagingFlow = _youthPolicyPagingFlow.asStateFlow()

    private val _selectingFilters = MutableStateFlow(PolicyFilters())
    val selectingFilters: StateFlow<PolicyFiltersUiModel> =
        _selectingFilters.map { it.toUiModel() }.stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(),
            PolicyFiltersUiModel(),
        )

    private var completedFilters = PolicyFilters()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _youthPolicyPagingFlow.update {
                youthPoliciesUseCase(
                    filterInfo = completedFilters,
                    onError = {
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
}

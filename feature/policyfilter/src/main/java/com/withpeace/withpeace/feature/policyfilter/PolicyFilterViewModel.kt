package com.withpeace.withpeace.feature.policyfilter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.domain.usecase.UpdatePolicyFilterUseCase
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.core.ui.policy.filtersetting.PolicyFiltersUiModel
import com.withpeace.withpeace.core.ui.policy.filtersetting.toDomain
import com.withpeace.withpeace.core.ui.policy.filtersetting.toUiModel
import com.withpeace.withpeace.core.ui.policy.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PolicyFilterViewModel @Inject constructor(
    private val policyFilterUseCase: UpdatePolicyFilterUseCase,
) : ViewModel() {
    private val _uiEvent = Channel<PolicyFilterUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _selectingFilters = MutableStateFlow(PolicyFilters())
    val selectingFilters: StateFlow<PolicyFiltersUiModel> =
        _selectingFilters.map { it.toUiModel() }.stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(),
            PolicyFiltersUiModel(),
        )

    fun setUpPolicy() { // 가입 완료
        viewModelScope.launch {
            policyFilterUseCase(
                selectingFilters.value.toDomain(),
                onError = {},
            ).collect {
                _uiEvent.send(PolicyFilterUiEvent.Success)
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

}
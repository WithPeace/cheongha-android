package com.withpeace.withpeace.feature.policydetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.GetYouthPolicyDetailUseCase
import com.withpeace.withpeace.feature.policydetail.navigation.POLICY_DETAIL_YOUTH_POLICY_ID_ARGUMENT
import com.withpeace.withpeace.feature.policydetail.uistate.YouthPolicyDetailUiEvent
import com.withpeace.withpeace.feature.policydetail.uistate.YouthPolicyDetailUiState
import com.withpeace.withpeace.feature.policydetail.uistate.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PolicyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPolicyDetailUseCase: GetYouthPolicyDetailUseCase,
) : ViewModel() {
    private val policyId = savedStateHandle.get<String>(POLICY_DETAIL_YOUTH_POLICY_ID_ARGUMENT)
        ?: throw IllegalArgumentException("Id를 불러올 수 없습니다.")

    private val _policyDetailUiState: MutableStateFlow<YouthPolicyDetailUiState> =
        MutableStateFlow(YouthPolicyDetailUiState.Loading)
    val policyDetailUiState = _policyDetailUiState.asStateFlow()

    private val _uiEvent = Channel<YouthPolicyDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        fetchPolicyDetail(policyId)
    }

    private fun fetchPolicyDetail(policyId: String) {
        viewModelScope.launch {
            getPolicyDetailUseCase(
                policyId = policyId,
                onError = {

                },
            ).collect { data ->
                _policyDetailUiState.update { YouthPolicyDetailUiState.Success(data.toUiModel()) }
            }
        }
    }
}
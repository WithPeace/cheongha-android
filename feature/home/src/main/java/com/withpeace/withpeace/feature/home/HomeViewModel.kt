package com.withpeace.withpeace.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.withpeace.withpeace.core.domain.usecase.GetYouthPoliciesUseCase
import com.withpeace.withpeace.feature.home.navigation.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val youthPoliciesUseCase: GetYouthPoliciesUseCase,
) : ViewModel() {
    private val _youthPolicyPagingFlow = MutableStateFlow(PagingData.empty<YouthPolicyUiModel>())
    val youthPolicyPagingFlow = _youthPolicyPagingFlow.asStateFlow()
    init {
        viewModelScope.launch {
            _youthPolicyPagingFlow.update {
                youthPoliciesUseCase(
                    policyClassifications = listOf(),
                    policyRegions = listOf(),
                    onError = {
                    },
                ).map {
                    it.map {
                        it.toUiModel()
                    }
                }.cachedIn(viewModelScope).firstOrNull() ?: PagingData.empty()
            }
        }
    }
}
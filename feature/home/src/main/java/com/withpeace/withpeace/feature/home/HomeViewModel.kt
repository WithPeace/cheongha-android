package com.withpeace.withpeace.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.GetYouthPoliciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val youthPoliciesUseCase: GetYouthPoliciesUseCase,
) : ViewModel() {
    init {
        viewModelScope.launch {
            youthPoliciesUseCase(
                policyClassifications = listOf(),
                policyRegions = listOf(),
                onError = {

                },
            ).collect {

            }
        }
    }
}
package com.withpeace.withpeace.feature.policyconsent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.feature.policyconsent.uistate.PolicyConsentUiEvent
import com.withpeace.withpeace.feature.policyconsent.uistate.PolicyConsentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PolicyConsentViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        PolicyConsentUiState(
            allChecked = false, termsOfServiceChecked = false, privacyPolicyChecked = false,
        ),
    )
    val uiState = _uiState.asStateFlow()

    private val _policyConsentUiEvent = Channel<PolicyConsentUiEvent>()
    val policyConsentEvent = _policyConsentUiEvent.receiveAsFlow()

    fun onAllChecked(checked: Boolean) {
        _uiState.update {
            it.copy(
                allChecked = checked,
                termsOfServiceChecked = checked,
                privacyPolicyChecked = checked,
            )
        }
    }

    fun onPrivacyPolicyChecked(checked: Boolean) {
        _uiState.update {
            if (it.termsOfServiceChecked && checked) // 자식이 모두 체크된 경우
                it.copy(
                    allChecked = true,
                    privacyPolicyChecked = true,
                )
            else it.copy(
                allChecked = false,
                privacyPolicyChecked = checked,
            )
        }
    }

    fun onTermsOfServiceChecked(checked: Boolean) {
        _uiState.update {
            if (it.privacyPolicyChecked && checked)
                it.copy(
                    allChecked = true,
                    termsOfServiceChecked = true,
                )
            else it.copy(
                allChecked = false,
                termsOfServiceChecked = checked,
            )
        }
    }

    fun checkToNext() {
        viewModelScope.launch {
            if (uiState.value.allChecked) _policyConsentUiEvent.send(PolicyConsentUiEvent.SuccessToNext)
            else _policyConsentUiEvent.send(PolicyConsentUiEvent.FailureToNext)
        }
    }
}
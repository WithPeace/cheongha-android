package com.withpeace.withpeace.feature.policybookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.GetBookmarkedPolicyUseCase
import com.withpeace.withpeace.feature.policybookmarks.uistate.BookmarkedPolicyUIState
import com.withpeace.withpeace.feature.policybookmarks.uistate.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class PolicyBookmarkViewModel @Inject constructor(
    private val getBookmarkedPolicyUseCase: GetBookmarkedPolicyUseCase,
) : ViewModel() {
    private val _bookmarkedPolicies: MutableStateFlow<BookmarkedPolicyUIState> =
        MutableStateFlow<BookmarkedPolicyUIState>(BookmarkedPolicyUIState.Loading)
    val bookmarkedPolicies = _bookmarkedPolicies.asStateFlow()

    fun getBookmarkedPolicies() {
        viewModelScope.launch {
            getBookmarkedPolicyUseCase(
                onError = {

                },
            ).collect { bookmarkedPolicies ->
                // TODO()
                _bookmarkedPolicies.update {
                    BookmarkedPolicyUIState.Success(bookmarkedPolicies.map { it.toUiModel() })
                }
            }
        }
    }
}
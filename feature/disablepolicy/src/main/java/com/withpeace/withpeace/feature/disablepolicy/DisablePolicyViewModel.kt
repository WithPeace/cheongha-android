package com.withpeace.withpeace.feature.disablepolicy

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.BookmarkPolicyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisablePolicyViewModel @Inject constructor(
    private val bookmarkPolicyUseCase: BookmarkPolicyUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun unBookmark() {
        viewModelScope.launch {
            bookmarkPolicyUseCase(
                policyId = "", isBookmarked = false,
                onError = {

                },
            )
        }
    }
}
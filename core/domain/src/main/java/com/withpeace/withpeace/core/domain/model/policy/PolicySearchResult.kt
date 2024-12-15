package com.withpeace.withpeace.core.domain.model.policy

import androidx.paging.PagingData

data class PolicySearchResult(
    val resultCount: Int,
    val policies: PagingData<YouthPolicy>,
)

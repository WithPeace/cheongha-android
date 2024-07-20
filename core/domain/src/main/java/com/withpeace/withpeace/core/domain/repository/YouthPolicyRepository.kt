package com.withpeace.withpeace.core.domain.repository

import androidx.paging.PagingData
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicyDetail
import kotlinx.coroutines.flow.Flow

interface YouthPolicyRepository {
    fun getPolicies(
        filterInfo: PolicyFilters,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<PagingData<YouthPolicy>>

    fun getPolicy(
        policyId: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<YouthPolicyDetail>
}
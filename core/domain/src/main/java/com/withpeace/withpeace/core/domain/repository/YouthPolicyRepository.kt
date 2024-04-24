package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import kotlinx.coroutines.flow.Flow

interface YouthPolicyRepository {
    fun getPolicies(
        onError: suspend (CheonghaError) -> Unit
    ): Flow<List<YouthPolicy>>
}
package com.withpeace.withpeace.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.withpeace.withpeace.core.data.paging.YouthPolicyPagingSource
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.policy.PolicyClassification
import com.withpeace.withpeace.core.domain.model.policy.PolicyRegion
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import com.withpeace.withpeace.core.network.di.service.YouthPolicyService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultYouthPolicyRepository @Inject constructor(
    private val youthPolicyService: YouthPolicyService,
) : YouthPolicyRepository {
    override fun getPolicies(
        policyClassifications: List<PolicyClassification>,
        policyRegions: List<PolicyRegion>,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<PagingData<YouthPolicy>> = Pager(
        config = PagingConfig(PAGE_SIZE),
        pagingSourceFactory = {
            YouthPolicyPagingSource(
                youthPolicyService = youthPolicyService,
                onError = onError,
            )
        },
    ).flow

    companion object {
        private const val PAGE_SIZE = 10
    }
}
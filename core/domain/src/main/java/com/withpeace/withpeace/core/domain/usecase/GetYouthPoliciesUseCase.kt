package com.withpeace.withpeace.core.domain.usecase

import androidx.paging.PagingData
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.policy.PolicyClassification
import com.withpeace.withpeace.core.domain.model.policy.PolicyRegion
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetYouthPoliciesUseCase @Inject constructor(
    private val youthPolicyRepository: YouthPolicyRepository,
) {
    operator fun invoke(
        policyClassifications: List<PolicyClassification>,
        policyRegions: List<PolicyRegion>,
        onError: (CheonghaError) -> Unit,
    ): Flow<PagingData<YouthPolicy>> =
        youthPolicyRepository.getPolicies(
            policyClassifications = policyClassifications,
            policyRegions = policyRegions,
            onError = onError,
        )
}
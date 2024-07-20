package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicyDetail
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetYouthPolicyDetailUseCase @Inject constructor(
    private val youthPolicyRepository: YouthPolicyRepository,
) {
    operator fun invoke(
        policyId: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<YouthPolicyDetail> {
        return youthPolicyRepository.getPolicy(policyId = policyId, onError = onError)
    }
}
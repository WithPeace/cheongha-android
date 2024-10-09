package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import javax.inject.Inject

class GetRecommendPoliciesUseCase @Inject constructor(
    private val policyRepository: YouthPolicyRepository,
) {
    operator fun invoke(onError: suspend (CheonghaError) -> Unit) =
        policyRepository.getRecommendPolicy(onError)
}
package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdatePolicyFilterUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        policyFilters: PolicyFilters,
        onError: (CheonghaError) -> Unit,
    ): Flow<Unit> {
        return userRepository.updatePolicyFilter(policyFilters = policyFilters, onError = onError)
    }
}
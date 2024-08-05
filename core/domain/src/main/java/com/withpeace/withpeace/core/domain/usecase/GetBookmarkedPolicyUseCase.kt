package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.policy.BookmarkedPolicy
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarkedPolicyUseCase @Inject constructor(
    private val policyRepository: YouthPolicyRepository,
) {
    operator fun invoke(
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<List<BookmarkedPolicy>> {
        return policyRepository.getBookmarkedPolicies(onError)
    }
}
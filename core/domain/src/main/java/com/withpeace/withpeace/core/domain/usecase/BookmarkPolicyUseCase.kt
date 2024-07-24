package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookmarkPolicyUseCase @Inject constructor(
    private val policyRepository: YouthPolicyRepository,
) {
    operator fun invoke(
        policyId: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit> {
        return policyRepository.bookmarkPolicy(policyId, onError)
    }
}
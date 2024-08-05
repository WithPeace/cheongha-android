package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.policy.BookmarkInfo
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookmarkPolicyUseCase @Inject constructor(
    private val policyRepository: YouthPolicyRepository,
) {
    suspend operator fun invoke(
        policyId: String,
        isBookmarked: Boolean,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<BookmarkInfo> {
        if(isBookmarked) policyRepository.bookmarkPolicy(policyId,onError).first()
        else policyRepository.unBookmarkPolicy(policyId, onError).first()
        return flow { emit(BookmarkInfo(policyId, isBookmarked))}
    }
}
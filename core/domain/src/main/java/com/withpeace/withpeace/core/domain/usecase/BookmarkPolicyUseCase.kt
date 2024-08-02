package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.policy.BookmarkInfo
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookmarkPolicyUseCase @Inject constructor(
    private val policyRepository: YouthPolicyRepository,
) {
    operator fun invoke(
        policyId: String,
        isBookmarked: Boolean,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<BookmarkInfo> = flow {
        if (isBookmarked) policyRepository.bookmarkPolicy(policyId, onError).collect { emit(BookmarkInfo(policyId, true)) }
        else policyRepository.unBookmarkPolicy(policyId, onError).collect { emit(BookmarkInfo(policyId, false)) }
    }
}
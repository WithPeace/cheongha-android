package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.policy.BookmarkInfo
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkPolicyUseCase @Inject constructor(
    private val policyRepository: YouthPolicyRepository,
) {
    operator fun invoke(
        policyId: String,
        isBookmarked: Boolean,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<BookmarkInfo> {
        return if (isBookmarked) policyRepository.bookmarkPolicy(policyId, onError)
            .map { BookmarkInfo(id = policyId, isBookmarked = true) }
        else policyRepository.unBookmarkPolicy(policyId, onError)
            .map { BookmarkInfo(id = policyId, isBookmarked = false) }
    }
}
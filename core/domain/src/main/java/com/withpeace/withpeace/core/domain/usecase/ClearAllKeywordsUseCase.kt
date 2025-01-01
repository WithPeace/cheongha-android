package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.RecentSearchKeywordRepository
import javax.inject.Inject

class ClearAllKeywordsUseCase @Inject constructor(
    private val repository: RecentSearchKeywordRepository
) {
    suspend operator fun invoke() {
        repository.clearAllKeywords()
    }
}
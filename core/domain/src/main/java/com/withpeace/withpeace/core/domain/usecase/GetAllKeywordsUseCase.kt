package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.search.SearchKeyword
import com.withpeace.withpeace.core.domain.repository.RecentSearchKeywordRepository
import javax.inject.Inject

class GetAllKeywordsUseCase @Inject constructor(
    private val repository: RecentSearchKeywordRepository,
) {
    suspend operator fun invoke(): List<SearchKeyword> {
        return repository.getAllKeywords()
    }
}
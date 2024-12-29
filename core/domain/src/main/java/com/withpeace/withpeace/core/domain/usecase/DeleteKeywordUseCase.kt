package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.search.SearchKeyword
import com.withpeace.withpeace.core.domain.repository.RecentSearchKeywordRepository
import javax.inject.Inject

class DeleteKeywordUseCase @Inject constructor(
    private val repository: RecentSearchKeywordRepository
) {
    suspend fun invoke(keyword: SearchKeyword) {
        repository.deleteKeyword(keyword)
    }
}
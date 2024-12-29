package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.search.SearchKeyword
import com.withpeace.withpeace.core.domain.repository.RecentSearchKeywordRepository
import javax.inject.Inject

class AddKeywordUseCase @Inject constructor(
    private val repository: RecentSearchKeywordRepository
) {
    suspend operator fun invoke(keyword: SearchKeyword) {
        repository.insertKeyword(keyword)
    }
}
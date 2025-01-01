package com.withpeace.withpeace.core.data.repository

import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.database.SearchKeywordDao
import com.withpeace.withpeace.core.database.SearchKeywordEntity
import com.withpeace.withpeace.core.domain.model.search.SearchKeyword
import com.withpeace.withpeace.core.domain.repository.RecentSearchKeywordRepository
import javax.inject.Inject

class DefaultRecentSearchKeywordRepository @Inject constructor(
    private val searchKeywordDao: SearchKeywordDao,
) : RecentSearchKeywordRepository {
    override suspend fun insertKeyword(keyword: SearchKeyword) {
        searchKeywordDao.insertKeyword(SearchKeywordEntity(keyword = keyword.value))
    }

    override suspend fun getAllKeywords(): List<SearchKeyword> {
        return searchKeywordDao.getAllKeywords().map { it.toDomain() }
    }

    override suspend fun deleteKeyword(keyword: SearchKeyword) {
        searchKeywordDao.deleteKeyword(
            SearchKeywordEntity(
                keyword = keyword.value
            ),
        )
    }

    override suspend fun clearAllKeywords() {
        searchKeywordDao.clearAllKeywords()
    }
}
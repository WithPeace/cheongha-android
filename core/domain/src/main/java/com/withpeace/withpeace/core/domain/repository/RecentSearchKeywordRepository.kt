package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.search.SearchKeyword

interface RecentSearchKeywordRepository {
    suspend fun insertKeyword(keyword: SearchKeyword)
    suspend fun getAllKeywords(): List<SearchKeyword>
    suspend fun deleteKeyword(keyword: SearchKeyword)
    suspend fun clearAllKeywords()
}

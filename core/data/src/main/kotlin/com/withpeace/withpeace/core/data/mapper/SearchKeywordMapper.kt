package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.database.SearchKeywordEntity
import com.withpeace.withpeace.core.domain.model.search.SearchKeyword

fun SearchKeywordEntity.toDomain(): SearchKeyword {
    return SearchKeyword(keyword)
}
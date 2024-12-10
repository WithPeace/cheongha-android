package com.withpeace.withpeace.core.domain.model.search

@JvmInline
value class SearchKeyword(
    val value: String,
) {
    init {
        require(value.length > 2) { "검색어가 2글자 미만입니다."}
    }
}
package com.withpeace.withpeace.core.domain.model.search

@JvmInline
value class SearchKeyword(
    val value: String,
) {
    init {
        require(validate(value)) { "검색어가 2글자 미만입니다." }
    }

    companion object {
        fun validate(keyword: String): Boolean {
            return keyword.length > 1
        }
    }
}
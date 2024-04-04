package com.withpeace.withpeace.core.domain.model.post

import androidx.paging.PagingConfig
import androidx.paging.PagingSource

data class PostPageInfo(
    val pageSize: Int,
    val enablePlaceholders: Boolean = true,
    val pagingSource: PagingSource<Int, Post>,
) {
    val pagingConfig =
        PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = enablePlaceholders,
        )
}

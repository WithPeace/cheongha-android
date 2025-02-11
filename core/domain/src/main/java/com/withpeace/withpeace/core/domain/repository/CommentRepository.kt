package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.post.ReportType
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun registerComment(
        targetType: String,
        targetId: Long,
        content: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit>

    fun reportComment(
        commentId: Long,
        reportType: ReportType,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit>
}
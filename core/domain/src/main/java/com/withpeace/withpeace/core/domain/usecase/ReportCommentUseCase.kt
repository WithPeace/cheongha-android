package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.post.ReportType
import com.withpeace.withpeace.core.domain.repository.CommentRepository
import javax.inject.Inject

class ReportCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    operator fun invoke(
        commentId: Long,
        reportType: ReportType,
        onError: suspend (CheonghaError) -> Unit,
    ) = commentRepository.reportComment(
        commentId = commentId,
        reportType = reportType,
        onError = onError,
    )
}
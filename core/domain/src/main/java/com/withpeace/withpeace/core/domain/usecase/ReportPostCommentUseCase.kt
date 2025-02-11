package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.post.ReportType
import com.withpeace.withpeace.core.domain.repository.PostRepository
import javax.inject.Inject

class ReportPostCommentUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(
        commentId: Long,
        reportType: ReportType,
        onError: suspend (CheonghaError) -> Unit,
    ) = postRepository.reportComment(commentId, reportType, onError)
}

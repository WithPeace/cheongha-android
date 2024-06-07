package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.post.ReportType
import com.withpeace.withpeace.core.domain.repository.PostRepository
import javax.inject.Inject

class ReportPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(
        postId: Long,
        reportType: ReportType,
        onError: suspend (CheonghaError) -> Unit,
    ) = postRepository.reportPost(postId, reportType, onError)
}

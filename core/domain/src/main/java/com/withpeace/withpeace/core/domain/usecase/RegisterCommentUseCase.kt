package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.CommentRepository
import javax.inject.Inject

class RegisterCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    operator fun invoke(
        targetType: String,
        targetId: Long,
        content: String,
        onError: suspend (CheonghaError) -> Unit,
    ) = commentRepository.registerComment(
        targetType = targetType,
        targetId = targetId,
        content = content,
        onError = onError,
    )
}
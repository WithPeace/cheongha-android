package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.PostRepository
import javax.inject.Inject

class RegisterCommentUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(
        postId: Long,
        content: String,
        onError: suspend (CheonghaError) -> Unit,
    ) = postRepository.registerComment(
        postId = postId,
        content = content,
        onError = onError,
    )
}

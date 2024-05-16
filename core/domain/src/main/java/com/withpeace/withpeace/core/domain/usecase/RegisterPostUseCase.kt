package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import com.withpeace.withpeace.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(
        post: RegisterPost,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Long> = postRepository.registerPost(post = post, onError = onError)
}

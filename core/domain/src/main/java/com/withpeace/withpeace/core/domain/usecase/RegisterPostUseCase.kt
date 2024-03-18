package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import com.withpeace.withpeace.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(
        post: RegisterPost,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Long> = postRepository.registerPost(post, onError = onError)
}

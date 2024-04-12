package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(
        postId: Long,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Boolean> = postRepository.deletePost(postId, onError)
}

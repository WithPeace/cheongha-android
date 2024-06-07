package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.post.PostDetail
import com.withpeace.withpeace.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(
        postId: Long,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<PostDetail> = postRepository.getPostDetail(
        postId,
        onError,
    )
}

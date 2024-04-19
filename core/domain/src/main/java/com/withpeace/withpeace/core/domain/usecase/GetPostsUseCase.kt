package com.withpeace.withpeace.core.domain.usecase

import androidx.paging.PagingData
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(
        postTopic: PostTopic,
        pageSize: Int,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<PagingData<Post>> = postRepository.getPosts(
        postTopic = postTopic,
        pageSize = pageSize,
        onError = onError,
    )
}

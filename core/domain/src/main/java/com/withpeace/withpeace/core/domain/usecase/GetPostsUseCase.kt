package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.post.PostPageInfo
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.paging.PostPagingSource
import com.withpeace.withpeace.core.domain.repository.PostRepository
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(
        postTopic: PostTopic,
        pageSize: Int,
        onError: suspend (WithPeaceError) -> Unit,
    ): PostPageInfo = PostPageInfo(
        pageSize = pageSize,
        pagingSource = PostPagingSource(
            postRepository = postRepository,
            postTopic = postTopic,
            pageSize = pageSize,
            onError = onError,
        ),
    )
}

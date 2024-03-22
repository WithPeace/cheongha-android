package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.post.PostDetail
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun registerPost(
        post: RegisterPost,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Long>

    fun getPostDetail(
        postId: Long,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<PostDetail>
}

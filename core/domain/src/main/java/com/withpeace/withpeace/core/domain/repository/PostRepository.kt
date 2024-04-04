package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(
        postTopic: PostTopic,
        pageIndex: Int,
        pageSize: Int,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<List<Post>>

    fun registerPost(
        post: RegisterPost,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Long>
}

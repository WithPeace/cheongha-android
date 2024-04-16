package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostDetail
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(
        postTopic: PostTopic,
        pageIndex: Int,
        pageSize: Int,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<List<Post>>

    fun registerPost(
        post: RegisterPost,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Long>

    fun getPostDetail(
        postId: Long,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<PostDetail>

    fun deletePost(
        postId: Long,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Boolean>
}

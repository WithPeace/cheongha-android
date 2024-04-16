package com.withpeace.withpeace.core.domain.repository

import androidx.paging.PagingData
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostDetail
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(
        postTopic: PostTopic,
        pageSize: Int,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<PagingData<Post>>

    fun registerPost(
        post: RegisterPost,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Long>

    fun getPostDetail(
        postId: Long,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<PostDetail>

    fun deletePost(
        postId: Long,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Boolean>
}

package com.withpeace.withpeace.core.domain.repository

import androidx.paging.PagingData
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostDetail
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.model.post.RecentPost
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import com.withpeace.withpeace.core.domain.model.post.ReportType
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(
        postTopic: PostTopic,
        pageSize: Int,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<PagingData<Post>>

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

    fun registerComment(
        postId: Long,
        content: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Boolean>

    fun reportPost(
        postId: Long,
        reportType: ReportType,
        onError: suspend (CheonghaError) -> Unit
    ): Flow<Boolean>

    fun reportComment(
        commentId: Long,
        reportType: ReportType,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Boolean>

    fun getRecentPost(
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<List<RecentPost>>
}

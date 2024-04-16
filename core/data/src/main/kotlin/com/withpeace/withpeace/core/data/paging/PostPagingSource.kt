package com.withpeace.withpeace.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.network.di.service.PostService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PostPagingSource(
    private val postService: PostService,
    private val postTopic: PostTopic,
    private val pageSize: Int,
    private val onError: suspend (WithPeaceError) -> Unit,
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val currentPage = params.key ?: STARTING_PAGE_INDEX
            val data = getPosts(
                postTopic = postTopic,
                pageIndex = currentPage,
                pageSize = params.loadSize,
                onError = onError,
            ).firstOrNull() ?: emptyList()
            val endOfPaginationReached = data.isEmpty()
            val prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1
            val nextKey =
                if (endOfPaginationReached) null else currentPage + (params.loadSize / pageSize)
            LoadResult.Page(data, prevKey, nextKey)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition
    }

    private fun getPosts(
        postTopic: PostTopic,
        pageIndex: Int,
        pageSize: Int,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<List<Post>> =
        flow {
            postService.getPosts(
                postTopic = postTopic.name,
                pageIndex = pageIndex, pageSize = pageSize,
            ).suspendMapSuccess {
                emit(data.map { it.toDomain() })
            }.suspendOnError {
                if (statusCode.code == 401) {
                    onError(
                        WithPeaceError.UnAuthorized(
                            statusCode.code,
                            message = null,
                        ),
                    )
                } else {
                    onError(WithPeaceError.GeneralError(statusCode.code, messageOrNull))
                }
            }.suspendOnException {
                onError(WithPeaceError.GeneralError(message = messageOrNull))
            }
        }.flowOn(Dispatchers.IO)

    companion object {
        const val STARTING_PAGE_INDEX = 0
    }
}

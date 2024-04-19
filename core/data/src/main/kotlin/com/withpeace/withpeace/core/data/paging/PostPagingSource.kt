package com.withpeace.withpeace.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnSuccess
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.data.util.handleApiFailure
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.network.di.service.PostService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PostPagingSource(
    private val postService: PostService,
    private val userRepository: UserRepository,
    private val postTopic: PostTopic,
    private val pageSize: Int,
    private val onError: suspend (CheonghaError) -> Unit,
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
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<List<Post>> =
        flow {
            postService.getPosts(
                postTopic = postTopic.name,
                pageIndex = pageIndex, pageSize = pageSize,
            ).suspendOnSuccess {
                val serverCurrentDate = this.headers["Date"].toString()
                val response = this.data
                emit(response.data.map { it.toDomain(serverCurrentDate) })
            }.handleApiFailure {
                onErrorWithAuthExpired(it, onError)
            }
        }.flowOn(Dispatchers.IO)

    private suspend fun onErrorWithAuthExpired(
        it: ResponseError,
        onError: suspend (CheonghaError) -> Unit,
    ) {
        if (it == ResponseError.INVALID_TOKEN_ERROR) {
            userRepository.logout(onError).collect {
                onError(ClientError.AuthExpired)
            }
        } else {
            onError(it)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 0
    }
}

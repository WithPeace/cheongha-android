package com.withpeace.withpeace.core.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.firstOrNull

data class PostPagingSource(
    private val postRepository: PostRepository,
    private val postTopic: PostTopic,
    private val pageSize: Int,
    private val onError: suspend (WithPeaceError) -> Unit,
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val currentPage = params.key ?: STARTING_PAGE_INDEX
            val data = postRepository.getPosts(
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
        return state.anchorPosition?.let { achorPosition ->
            state.closestPageToPosition(achorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(achorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}

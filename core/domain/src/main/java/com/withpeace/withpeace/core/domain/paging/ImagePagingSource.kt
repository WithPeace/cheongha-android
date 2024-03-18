package com.withpeace.withpeace.core.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.withpeace.withpeace.core.domain.repository.ImageRepository

data class ImagePagingSource(
    private val imageRepository: ImageRepository,
    private val folderName: String?,
) : PagingSource<Int, String>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        return try {
            val currentPage = params.key ?: STARTING_PAGE_INDEX
            val data = imageRepository.getImages(
                page = currentPage,
                loadSize = params.loadSize,
                folderName = folderName,
            )
            val endOfPaginationReached = data.isEmpty()
            val prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1
            val nextKey =
                if (endOfPaginationReached) null else currentPage + (params.loadSize / PAGING_SIZE)
            LoadResult.Page(data, prevKey, nextKey)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return state.anchorPosition?.let { achorPosition ->
            state.closestPageToPosition(achorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(achorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
        const val PAGING_SIZE = 30
    }
}

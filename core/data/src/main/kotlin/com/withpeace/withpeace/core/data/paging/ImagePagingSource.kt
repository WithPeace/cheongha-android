package com.withpeace.withpeace.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.domain.model.image.ImageInfo
import com.withpeace.withpeace.core.imagestorage.ImageDataSource

data class ImagePagingSource(
    private val imageDataSource: ImageDataSource,
    private val folderName: String?,
    private val pageSize: Int,
) : PagingSource<Int, ImageInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageInfo> {
        return try {
            val currentPage = params.key ?: STARTING_PAGE_INDEX
            val data = imageDataSource.getImages(
                page = currentPage,
                loadSize = params.loadSize,
                folder = folderName,
            ).map { it.toDomain() }
            val endOfPaginationReached = data.isEmpty()
            val prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1
            val nextKey =
                if (endOfPaginationReached) null else currentPage + (params.loadSize / pageSize)
            LoadResult.Page(data, prevKey, nextKey)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ImageInfo>): Int? {
        return state.anchorPosition?.let { achorPosition ->
            state.closestPageToPosition(achorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(achorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}

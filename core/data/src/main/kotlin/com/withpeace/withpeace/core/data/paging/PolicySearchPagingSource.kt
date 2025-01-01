package com.withpeace.withpeace.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.data.mapper.youthpolicy.toDomain
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.NoSearchResultException
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.network.di.service.YouthPolicyService

class PolicySearchPagingSource(
    private val pageSize: Int,
    private val youthPolicyService: YouthPolicyService,
    private val keyword: String,
    private val onError: suspend (CheonghaError) -> Unit,
    private val userRepository: UserRepository,
    private val onReceiveTotalCount: suspend (Int) -> Unit,
) : PagingSource<Int, Pair<Int, YouthPolicy>>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pair<Int, YouthPolicy>> {
        val pageIndex = params.key ?: 1
        val response = youthPolicyService.search(
            keyword = keyword,
            pageSize = params.loadSize,
            pageIndex = pageIndex,
        )

        if (response is ApiResponse.Success) {
            val successResponse = (response).data
            onReceiveTotalCount(successResponse.data.totalCount)
            if (response.data.data.totalCount == 0) {
                return LoadResult.Error(NoSearchResultException())
            }
            return LoadResult.Page(
                data = successResponse.data.policies.map {
                    Pair(
                        successResponse.data.totalCount,
                        it.toDomain(),
                    )
                },
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (successResponse.data.policies.isEmpty()) null else pageIndex + (params.loadSize / pageSize),
            )
        } else {
            return LoadResult.Error(IllegalStateException("api state error"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Pair<Int, YouthPolicy>>): Int? { // 현재 포지션에서 Refresh pageKey 설정
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
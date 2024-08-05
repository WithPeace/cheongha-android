package com.withpeace.withpeace.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.data.mapper.youthpolicy.toCode
import com.withpeace.withpeace.core.data.mapper.youthpolicy.toDomain
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.network.di.service.YouthPolicyService

class YouthPolicyPagingSource(
    private val pageSize: Int,
    private val youthPolicyService: YouthPolicyService,
    private val userRepository: UserRepository,
    private val filterInfo: PolicyFilters,
    private val onError: suspend (CheonghaError) -> Unit,
) :
    PagingSource<Int, YouthPolicy>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, YouthPolicy> {
        val pageIndex = params.key ?: 1
        val response = youthPolicyService.getPolicies(
            display = params.loadSize,
            pageIndex = pageIndex,
            region = filterInfo.regions.joinToString(",") { it.toCode() },
            classification = filterInfo.classifications.joinToString(",") { it.toCode() },
        )

        if (response is ApiResponse.Success) {
            val successResponse = (response).data
            return LoadResult.Page(
                data = successResponse.data.map { it.toDomain() },
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (successResponse.data.isEmpty()) null else pageIndex + (params.loadSize / pageSize),
            )
        } else {
            // 방법1 Error exception 으로 구분
            // 방법2 exception을 하단에서 방출
            return LoadResult.Error(IllegalStateException("api state error"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, YouthPolicy>): Int? { // 현재 포지션에서 Refresh pageKey 설정
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

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
        private const val STARTING_PAGE_INDEX = 1
    }
}

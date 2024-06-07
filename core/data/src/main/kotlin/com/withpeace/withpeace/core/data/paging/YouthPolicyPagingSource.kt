package com.withpeace.withpeace.core.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.data.BuildConfig
import com.withpeace.withpeace.core.data.mapper.toCode
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.network.di.service.YouthPolicyService

class YouthPolicyPagingSource(
    private val pageSize: Int,
    private val youthPolicyService: YouthPolicyService,
    private val filterInfo: PolicyFilters,
    private val onError: suspend (CheonghaError) -> Unit,
) :
    PagingSource<Int, YouthPolicy>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, YouthPolicy> {
        val pageIndex = params.key ?: 1
        val response = youthPolicyService.getPolicies(
            apiKey = BuildConfig.YOUTH_POLICY_API_KEY,
            pageSize = params.loadSize,
            pageIndex = pageIndex,
            classification = filterInfo.classifications.joinToString(",") { it.toCode() },
            region = filterInfo.regions.joinToString(",") { it.toCode() },
        )



        if (response is ApiResponse.Failure) {
            val error: CheonghaError =
                if (response is ApiResponse.Failure.Exception) ResponseError.HTTP_EXCEPTION_ERROR
                else ResponseError.UNKNOWN_ERROR //TODO("오류에 대해서 Firebase Logging")
            onError(error)
            return LoadResult.Error(IllegalStateException("API response error that $error"))
        }

        val data = (response as ApiResponse.Success).data
        return LoadResult.Page(
            data = data.youthPolicyEntity.map { it.toDomain() },
            prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
            nextKey = if (response.data.youthPolicyEntity.isEmpty()) null else pageIndex + (params.loadSize / pageSize),
        )
    }

    override fun getRefreshKey(state: PagingState<Int, YouthPolicy>): Int? { // 현재 포지션에서 Refresh pageKey 설정
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}

package com.withpeace.withpeace.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.withpeace.withpeace.core.data.mapper.youthpolicy.toDomain
import com.withpeace.withpeace.core.data.paging.YouthPolicyPagingSource
import com.withpeace.withpeace.core.data.util.handleApiFailure
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicyDetail
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import com.withpeace.withpeace.core.network.di.service.YouthPolicyService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultYouthPolicyRepository @Inject constructor(
    private val youthPolicyService: YouthPolicyService,
    private val userRepository: UserRepository,
) : YouthPolicyRepository {
    override fun getPolicies(
        filterInfo: PolicyFilters,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<PagingData<YouthPolicy>> = Pager(
        config = PagingConfig(PAGE_SIZE),
        pagingSourceFactory = {
            YouthPolicyPagingSource(
                youthPolicyService = youthPolicyService,
                onError = onError,
                pageSize = PAGE_SIZE,
                filterInfo = filterInfo,
            )
        },
    ).flow

    override fun getPolicy(
        policyId: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<YouthPolicyDetail> = flow {
        youthPolicyService.getPolicyDetail(policyId).suspendMapSuccess {
            emit(data.toDomain())
        }.handleApiFailure {
            onErrorWithAuthExpired(it, onError)
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
        private const val PAGE_SIZE = 10
    }
}
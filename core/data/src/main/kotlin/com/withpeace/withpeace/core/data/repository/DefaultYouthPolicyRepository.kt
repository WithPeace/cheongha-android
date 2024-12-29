package com.withpeace.withpeace.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skydoves.sandwich.suspendMapSuccess
import com.withpeace.withpeace.core.data.mapper.youthpolicy.toDomain
import com.withpeace.withpeace.core.data.paging.PolicySearchPagingSource
import com.withpeace.withpeace.core.data.paging.YouthPolicyPagingSource
import com.withpeace.withpeace.core.data.util.handleApiFailure
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.policy.BookmarkedPolicy
import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicyDetail
import com.withpeace.withpeace.core.domain.model.search.SearchKeyword
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
                userRepository = userRepository,
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

    override fun getBookmarkedPolicies(
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<List<BookmarkedPolicy>> = flow {
        youthPolicyService.getBookmarkedPolicies().suspendMapSuccess {
            emit(data.map { it.toDomain() })
        }.handleApiFailure {
            onErrorWithAuthExpired(it, onError)
        }
    }

    override fun bookmarkPolicy(
        policyId: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit> = flow {
        youthPolicyService.bookmarkPolicy(policyId).suspendMapSuccess {
            emit(Unit)
        }.handleApiFailure {
            onErrorWithAuthExpired(it, onError)
        }
    }

    override fun unBookmarkPolicy(
        policyId: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit> = flow {
        youthPolicyService.unBookmarkPolicy(policyId).suspendMapSuccess {
            emit(Unit)
        }.handleApiFailure {
            onErrorWithAuthExpired(it, onError)
        }
    }

    override fun getRecommendPolicy(onError: suspend (CheonghaError) -> Unit): Flow<List<YouthPolicy>> =
        flow {
            youthPolicyService.getRecommendations().suspendMapSuccess {
                emit(data.map { it.toDomain() })
            }.handleApiFailure {
                onErrorWithAuthExpired(it, onError)
            }
        }

    override fun getHotPolicy(onError: suspend (CheonghaError) -> Unit): Flow<List<YouthPolicy>> =
        flow {
            youthPolicyService.getHots().suspendMapSuccess {
                emit(data.map { it.toDomain() })
            }.handleApiFailure {
                onErrorWithAuthExpired(it, onError)
            }
        }

    override fun search(
        searchKeyword: SearchKeyword,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<PagingData<YouthPolicy>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = {
                PolicySearchPagingSource(
                    keyword = searchKeyword.value,
                    youthPolicyService = youthPolicyService,
                    onError = onError,
                    userRepository = userRepository,
                    pageSize = PAGE_SIZE,
                )
            },
        ).flow
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
package com.withpeace.withpeace.core.data.repository

import com.skydoves.sandwich.suspendMapSuccess
import com.withpeace.withpeace.core.data.BuildConfig
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import com.withpeace.withpeace.core.network.di.service.YouthPolicyService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultYouthPolicyRepository @Inject constructor(
    private val youthPolicyService: YouthPolicyService,
) : YouthPolicyRepository {
    override fun getPolicies(
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<List<YouthPolicy>> = flow {
        youthPolicyService.getPolicies(
            BuildConfig.YOUTH_POLICY_API_KEY,
            10,
            1,
        ).suspendMapSuccess {
            emit(this.youthPolicyEntity.map {
                it.toDomain()
            })
        }
    }
}
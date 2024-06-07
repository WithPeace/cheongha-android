package com.withpeace.withpeace.core.data.repository

import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnFailure
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.repository.AppUpdateRepository
import com.withpeace.withpeace.core.network.di.service.AppUpdateService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultAppUpdateRepository @Inject constructor(
    private val appUpdateService: AppUpdateService,
) : AppUpdateRepository {
    override fun shouldUpdate(
        onError: suspend (CheonghaError) -> Unit,
        currentVersion: Int,
    ): Flow<Boolean> = flow {
        appUpdateService.shouldUpdate(currentVersion).suspendMapSuccess {
            emit(this.data)
        }.suspendOnFailure {
            onError(ResponseError.UNKNOWN_ERROR)
        }
    }
}

package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.AppUpdateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckAppUpdateUseCase @Inject constructor(
    private val appUpdateRepository: AppUpdateRepository,
) {
    operator fun invoke(
        currentVersion: Int,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Boolean> = appUpdateRepository.shouldUpdate(onError, currentVersion)
}
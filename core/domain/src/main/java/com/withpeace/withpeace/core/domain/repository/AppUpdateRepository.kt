package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import kotlinx.coroutines.flow.Flow

interface AppUpdateRepository {
    fun shouldUpdate(onError: suspend (CheonghaError) -> Unit, currentVersion: Int): Flow<Boolean>
}
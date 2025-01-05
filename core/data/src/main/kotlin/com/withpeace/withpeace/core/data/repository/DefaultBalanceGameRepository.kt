package com.withpeace.withpeace.core.data.repository

import com.withpeace.withpeace.core.datastore.dataStore.balancegame.BalanceGameDataStore
import com.withpeace.withpeace.core.domain.repository.BalanceGameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultBalanceGameRepository @Inject constructor(
    private val balanceGameDataStore: BalanceGameDataStore,
): BalanceGameRepository {
    override fun isVisited(): Flow<Boolean> {
        return balanceGameDataStore.isVisited
    }

    override suspend fun updateVisitedStatus(visited: Boolean) {
        balanceGameDataStore.updateVisitedStatus(visited)
    }
}
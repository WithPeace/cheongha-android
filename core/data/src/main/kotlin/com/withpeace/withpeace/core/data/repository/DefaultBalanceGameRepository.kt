package com.withpeace.withpeace.core.data.repository

import android.util.Log
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.datastore.dataStore.balancegame.BalanceGameDataStore
import com.withpeace.withpeace.core.domain.model.balancegame.BalanceGame
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.BalanceGameRepository
import com.withpeace.withpeace.core.network.di.request.SelectBalanceGameRequest
import com.withpeace.withpeace.core.network.di.service.BalanceGameService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultBalanceGameRepository @Inject constructor(
    private val balanceGameDataStore: BalanceGameDataStore,
    private val balanceGameService: BalanceGameService,
): BalanceGameRepository {
    override fun isVisited(): Flow<Boolean> {
        return balanceGameDataStore.isVisited
    }

    override suspend fun updateVisitedStatus(visited: Boolean) {
        balanceGameDataStore.updateVisitedStatus(visited)
    }

    override fun fetchBalanceGame(
        pageIndex: Int,
        pageSize: Int,
        onError: (CheonghaError) -> Unit,
    ): Flow<List<BalanceGame>> = flow {
        balanceGameService.fetchBalanceGame(pageIndex = pageIndex, pageSize = pageSize).suspendMapSuccess {
            emit(this.data.map { it.toDomain() })
        }.suspendOnFailure {
            Log.d("test", this.message())
        }
    }

    override fun selectBalanceGame(
        gameId: String,
        selection: String,
        onError: (CheonghaError) -> Unit,
    ): Flow<Unit> = flow {
        balanceGameService.postBalanceGame(
            gameId,
            SelectBalanceGameRequest(selection)).suspendOnSuccess {
                emit(Unit)
        }
    }
}
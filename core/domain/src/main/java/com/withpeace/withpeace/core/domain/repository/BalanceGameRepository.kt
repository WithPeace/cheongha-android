package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.balancegame.BalanceGame
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import kotlinx.coroutines.flow.Flow

interface BalanceGameRepository {
    fun isVisited(): Flow<Boolean>

    suspend fun updateVisitedStatus(visited: Boolean)

    fun fetchBalanceGame(
        pageIndex: Int,
        pageSize: Int,
        onError: (CheonghaError) -> Unit,
    ): Flow<List<BalanceGame>>

    fun selectBalanceGame(
        gameId: Long,
        selection: String,
        onError: (CheonghaError) -> Unit,
    ): Flow<Unit>
}
package com.withpeace.withpeace.core.data.repository

import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnSuccess
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.data.util.handleApiFailure
import com.withpeace.withpeace.core.datastore.dataStore.balancegame.BalanceGameDataStore
import com.withpeace.withpeace.core.domain.model.balancegame.BalanceGame
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.repository.BalanceGameRepository
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.network.di.request.SelectBalanceGameRequest
import com.withpeace.withpeace.core.network.di.service.BalanceGameService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultBalanceGameRepository @Inject constructor(
    private val balanceGameDataStore: BalanceGameDataStore,
    private val balanceGameService: BalanceGameService,
    private val userRepository: UserRepository,
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
        }.handleApiFailure {
            onErrorWithAuthExpired(it, onError)
            if(it.serverErrorCode == 40010) {
                onError(ClientError.BalanceGameExpired)
            }
        }
    }

    override fun selectBalanceGame(
        gameId: Long,
        selection: String,
        onError: (CheonghaError) -> Unit,
    ): Flow<Unit> = flow {
        balanceGameService.postBalanceGame(
            gameId,
            SelectBalanceGameRequest(selection)).suspendOnSuccess {
                emit(Unit)
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
}
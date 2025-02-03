package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.balancegame.BalanceGame
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.BalanceGameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBalanceGameUseCase @Inject constructor(
    private val balanceGameRepository: BalanceGameRepository,
) {
    operator fun invoke(
        pageIndex: Int,
        pageSize: Int,
        onError: (CheonghaError) -> Unit,
    ): Flow<List<BalanceGame>> {
        return balanceGameRepository.fetchBalanceGame(
            pageIndex = pageIndex, pageSize = pageSize, onError = onError,
        )
    }
}
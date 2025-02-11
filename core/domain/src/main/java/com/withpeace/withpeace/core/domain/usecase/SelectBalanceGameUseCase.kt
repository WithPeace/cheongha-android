package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.BalanceGameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SelectBalanceGameUseCase @Inject constructor(
    private val balanceGameRepository: BalanceGameRepository,
) {
    operator fun invoke(
        gameId: Long,
        selection: String,
        onError: (CheonghaError) -> Unit,
    ): Flow<Unit> {
        return balanceGameRepository.selectBalanceGame(
            gameId = gameId,
            selection = selection,
            onError = onError,
        )
    }
}
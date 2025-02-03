package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.BalanceGameRepository
import javax.inject.Inject

class SelectBalanceGameUseCase @Inject constructor(
    private val balanceGameRepository: BalanceGameRepository,
) {
    operator fun invoke(
        gameId: String,
        selection: String, onError: (CheonghaError) -> Unit,
    ) {
        balanceGameRepository.selectBalanceGame(
            gameId = gameId,
            selection = selection,
            onError = onError,
        )
    }
}
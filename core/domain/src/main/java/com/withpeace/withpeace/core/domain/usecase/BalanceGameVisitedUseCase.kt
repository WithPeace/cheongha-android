package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.BalanceGameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BalanceGameVisitedUseCase @Inject constructor(
    private val balanceGameRepository: BalanceGameRepository,
) {
    operator fun invoke(): Flow<Boolean> = balanceGameRepository.isVisited()
}
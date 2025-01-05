package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.BalanceGameRepository
import javax.inject.Inject

class UpdateBalanceGameVisitedStatus @Inject constructor(
    private val balanceGameRepository: BalanceGameRepository,
){
    suspend operator fun invoke(isVisited: Boolean) {
        balanceGameRepository.updateVisitedStatus(isVisited)
    }
}
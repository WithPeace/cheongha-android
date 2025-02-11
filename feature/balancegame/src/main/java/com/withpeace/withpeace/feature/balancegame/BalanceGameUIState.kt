package com.withpeace.withpeace.feature.balancegame

sealed interface BalanceGameUIState {
    data class Success(val games: List<BalanceGameUiModel>): BalanceGameUIState
    data object Loading : BalanceGameUIState
    data object Failure: BalanceGameUIState
}
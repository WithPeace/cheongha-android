package com.withpeace.withpeace.feature.balancegame

sealed interface BalanceGameUiEvent {
    data object NextPage : BalanceGameUiEvent
    data object PreviousPage : BalanceGameUiEvent
}
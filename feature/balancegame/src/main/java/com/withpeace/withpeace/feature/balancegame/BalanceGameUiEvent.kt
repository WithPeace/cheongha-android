package com.withpeace.withpeace.feature.balancegame

sealed interface BalanceGameUiEvent {
    data object NextPage : BalanceGameUiEvent
    data object PreviousPage : BalanceGameUiEvent
    data object UnAuthorized : BalanceGameUiEvent
    data object RegisterCommentSuccess : BalanceGameUiEvent
    data object ReportCommentSuccess : BalanceGameUiEvent
    data object ReportCommentFailure : BalanceGameUiEvent
    data object RegisterCommentFailure : BalanceGameUiEvent
    data object ReportCommentDuplicated : BalanceGameUiEvent
}
package com.withpeace.withpeace

sealed interface MainUiState {
    data object Login : MainUiState
    data object Update : MainUiState
    data object Error : MainUiState
    data object Home : MainUiState
    data object Loading : MainUiState
}
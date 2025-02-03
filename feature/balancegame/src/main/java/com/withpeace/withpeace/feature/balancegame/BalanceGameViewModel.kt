package com.withpeace.withpeace.feature.balancegame

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.usecase.GetBalanceGameUseCase
import com.withpeace.withpeace.core.domain.usecase.SelectBalanceGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceGameViewModel @Inject constructor(
    private val getBalanceGameUseCase: GetBalanceGameUseCase,
    private val selectBalanceGameUseCase: SelectBalanceGameUseCase,
) : ViewModel() {
    private val _currentPage = MutableStateFlow<Int>(0)
    val currentPage = _currentPage.asStateFlow()

    private val _balanceGamesState: MutableStateFlow<BalanceGameUIState> =
        MutableStateFlow(BalanceGameUIState.Loading)
    val balanceGamesState = _balanceGamesState.asStateFlow()

    private val _uiEvent = Channel<BalanceGameUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var loadingAnchor = 0 // 로딩할 페이지

    init {
        fetchBalanceGame()
    }

    fun fetchBalanceGame(
    ) {
        viewModelScope.launch {
            getBalanceGameUseCase(
                pageIndex = loadingAnchor,
                pageSize = 3,
                onError = {

                },
            ).collect {
                if (balanceGamesState.value is BalanceGameUIState.Success) {
                    _balanceGamesState.value = BalanceGameUIState.Success(
                        it.reversed()
                            .map { it.toUIModel() } + (balanceGamesState.value as BalanceGameUIState.Success).games,
                    )
                    _currentPage.value = it.size-1
                    _uiEvent.send(BalanceGameUiEvent.PreviousPage)
                } else { // 최초 로딩시에
                    _currentPage.value = it.lastIndex
                    _balanceGamesState.value =
                        BalanceGameUIState.Success(it.reversed().map { it.toUIModel() })
                }
            }
        }
    }

    fun onClickBeforeDay() {
        viewModelScope.launch {
            if (balanceGamesState.value is BalanceGameUIState.Success) {
                // 아래가 currentPage == balanceGameState
                if ((balanceGamesState.value as BalanceGameUIState.Success).games.first().hasPrevious && currentPage.value == 0) {
                    loadingAnchor += 1
                    fetchBalanceGame()
                } else {
                    _currentPage.value -= 1
                    _uiEvent.send(BalanceGameUiEvent.PreviousPage)
                }
            }
        }
    }

    fun onClickAfterDay() {
        viewModelScope.launch {
            _uiEvent.send(BalanceGameUiEvent.NextPage)
        }
    }
}
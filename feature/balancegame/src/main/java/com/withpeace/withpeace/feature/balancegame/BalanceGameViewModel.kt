package com.withpeace.withpeace.feature.balancegame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.balancegame.BalanceGame
import com.withpeace.withpeace.core.domain.usecase.GetBalanceGameUseCase
import com.withpeace.withpeace.core.domain.usecase.SelectBalanceGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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

    private val lastGamesSelectionCache = mutableListOf<BalanceGame>()

    init {
        fetchBalanceGame()
    }

    private fun fetchBalanceGame() {
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
                            .map {
                                lastGamesSelectionCache.add(it)
                                it.toUIModel()
                            } + (balanceGamesState.value as BalanceGameUIState.Success).games,
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
            _currentPage.value += 1
        }
    }

    fun onSelectA(balanceGameUiModel: BalanceGameUiModel) {
        if (balanceGameUiModel.userChoice == "OPTION_A" || !balanceGameUiModel.date.contains("오늘의")) return

        val aCount = balanceGameUiModel.totalCount * balanceGameUiModel.percentageOptionA / 100
        val bCount = balanceGameUiModel.totalCount - aCount

        val newACount = aCount + 1
        val newBCount = if (balanceGameUiModel.userChoice == "OPTION_B") bCount - 1 else bCount
        val newTotalCount = newACount + newBCount

        val newData = balanceGameUiModel.copy(
            userChoice = "OPTION_A",
            totalCount = newTotalCount,
            percentageOptionA = (newACount * 100) / newTotalCount,
            percentageOptionB = (newBCount * 100) / newTotalCount
        )

        updateBalanceGameState(newData, balanceGameUiModel.gameId)

        viewModelScope.launch {
            selectBalanceGameUseCase(
                balanceGameUiModel.gameId,
                "OPTION_A",
                onError = { restorePreviousState(balanceGameUiModel.gameId) }
            ).collect {
                updateLastGamesCache(balanceGameUiModel.gameId, newData)
            }
        }
    }


    fun onSelectB(balanceGameUiModel: BalanceGameUiModel) {
        if (balanceGameUiModel.userChoice == "OPTION_B" || !balanceGameUiModel.date.contains("오늘의")) return

        val aCount = balanceGameUiModel.totalCount * balanceGameUiModel.percentageOptionA / 100
        val bCount = balanceGameUiModel.totalCount - aCount

        val newBCount = bCount + 1
        val newACount = if (balanceGameUiModel.userChoice == "OPTION_A") aCount - 1 else aCount
        val newTotalCount = newACount + newBCount

        val newData = balanceGameUiModel.copy(
            userChoice = "OPTION_B",
            totalCount = newTotalCount,
            percentageOptionA = (newACount * 100) / newTotalCount,
            percentageOptionB = (newBCount * 100) / newTotalCount
        )

        updateBalanceGameState(newData, balanceGameUiModel.gameId)

        viewModelScope.launch {
            selectBalanceGameUseCase(
                balanceGameUiModel.gameId,
                "OPTION_B",
                onError = { restorePreviousState(balanceGameUiModel.gameId) }
            ).collect {
                updateLastGamesCache(balanceGameUiModel.gameId, newData)
            }
        }
    }

    private fun updateBalanceGameState(newData: BalanceGameUiModel, gameId: Long) {
        _balanceGamesState.update {
            BalanceGameUIState.Success(
                (balanceGamesState.value as BalanceGameUIState.Success).games.map {
                    if (it.gameId == gameId) {
                        newData
                    } else {
                        it
                    }
                },
            )
        }
    }
    private fun restorePreviousState(gameId: Long) {
        _balanceGamesState.update {
            BalanceGameUIState.Success(
                (it as BalanceGameUIState.Success).games.map { game ->
                    lastGamesSelectionCache.find { it.id == gameId }?.toUIModel() ?: game
                }
            )
        }
    }
    private fun updateLastGamesCache(gameId: Long, newData: BalanceGameUiModel) {
        lastGamesSelectionCache.map {
            if (it.id == gameId) {
                return@map newData
            } else return@map it
        }
    }
}
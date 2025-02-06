package com.withpeace.withpeace.feature.balancegame

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.balancegame.BalanceGame
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.usecase.GetBalanceGameUseCase
import com.withpeace.withpeace.core.domain.usecase.GetCurrentUserIdUseCase
import com.withpeace.withpeace.core.domain.usecase.RegisterCommentUseCase
import com.withpeace.withpeace.core.domain.usecase.ReportCommentUseCase
import com.withpeace.withpeace.core.domain.usecase.SelectBalanceGameUseCase
import com.withpeace.withpeace.core.ui.post.CommentUiModel
import com.withpeace.withpeace.core.ui.post.ReportTypeUiModel
import com.withpeace.withpeace.core.ui.post.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceGameViewModel @Inject constructor(
    private val getBalanceGameUseCase: GetBalanceGameUseCase,
    private val selectBalanceGameUseCase: SelectBalanceGameUseCase,
    private val currentUserIdUseCase: GetCurrentUserIdUseCase,
    private val registerCommentUseCase: RegisterCommentUseCase,
    private val reportCommentUseCase: ReportCommentUseCase,
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

    private val _commentText = MutableStateFlow("")
    val commentText = _commentText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

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
                val userId = currentUserIdUseCase()
                if (balanceGamesState.value is BalanceGameUIState.Success) {
                    _balanceGamesState.value = BalanceGameUIState.Success(
                        it.reversed()
                            .map {
                                lastGamesSelectionCache.add(it)
                                it.toUIModel(userId)
                            } + (balanceGamesState.value as BalanceGameUIState.Success).games,
                    )
                    _currentPage.value = it.size-1
                    _uiEvent.send(BalanceGameUiEvent.PreviousPage)
                } else { // 최초 로딩시에
                    _currentPage.value = it.lastIndex
                    _balanceGamesState.value =
                        BalanceGameUIState.Success(it.reversed().map { it.toUIModel(userId) })
                }
            }
        }
    }

    private fun fetchCommentBalanceGame(onSuccess: suspend () -> Unit) {
        if (balanceGamesState.value is BalanceGameUIState.Success) {
            viewModelScope.launch {
                val currentState = balanceGamesState.value as BalanceGameUIState.Success
                val currentIndex = currentPage.value
                val userId = currentUserIdUseCase()

                // API 호출 시 필요한 인덱스 계산 (리스트 뒤집은 순서 기준)
                val gameIndexForApi = currentState.games.size - currentIndex - 1

                getBalanceGameUseCase(
                    pageIndex = gameIndexForApi,
                    pageSize = 1,
                    onError = {
                        // 에러 처리 필요 시 작성
                    },
                ).collect { updatedGames ->
                    if (updatedGames.isNotEmpty()) {
                        val updateGame = updatedGames.last()
                        _balanceGamesState.update { prevState ->
                            if (prevState is BalanceGameUIState.Success) {
                                val updatedList = prevState.games.toMutableList()
                                updatedList[currentIndex] = updateGame.toUIModel(userId)
                                BalanceGameUIState.Success(updatedList)
                            } else {
                                prevState
                            }
                        }
                        // 업데이트 성공 후 onSuccess 람다 호출
                        onSuccess()
                    }
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

    fun onCommentTextChanged(input: String) {
        _commentText.update { input }
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
        viewModelScope.launch {
            val userId = currentUserIdUseCase()
            _balanceGamesState.update {
                BalanceGameUIState.Success(
                    (it as BalanceGameUIState.Success).games.map { game ->
                        lastGamesSelectionCache.find { it.id == gameId }?.toUIModel(userId) ?: game
                    },
                )
            }
        }

    }
    private fun updateLastGamesCache(gameId: Long, newData: BalanceGameUiModel) {
        lastGamesSelectionCache.map {
            if (it.id == gameId) {
                return@map newData
            } else return@map it
        }
    }

    fun onClickCommentRegister() {
        if (commentText.value == "") return
        registerCommentUseCase(
            targetType = "BALANCE_GAME",
            content = commentText.value,
            onError = {
                Log.d("test",it.toString())
                when (it) {
                    ClientError.AuthExpired -> _uiEvent.send(BalanceGameUiEvent.UnAuthorized)
                    else -> _uiEvent.send(BalanceGameUiEvent.RegisterCommentFailure)
                }
            },
            targetId = (balanceGamesState.value as BalanceGameUIState.Success).games[currentPage.value].gameId,
        ).onStart {
            _isLoading.update { true }
        }.onEach {
            fetchCommentBalanceGame {
                _commentText.update { "" }
                _uiEvent.send(BalanceGameUiEvent.RegisterCommentSuccess)
            }
        }.onCompletion {
            _isLoading.update { false }
        }.launchIn(viewModelScope)
    }

    fun reportComment(
        commentId: Long,
        reportTypeUiModel: ReportTypeUiModel,
    ) {
        reportCommentUseCase(
            commentId,
            reportTypeUiModel.toDomain(),
            onError = {
                when (it) {
                    ResponseError.COMMENT_DUPLICATED_ERROR -> _uiEvent.send(BalanceGameUiEvent.ReportCommentFailure)
                    else -> _uiEvent.send(BalanceGameUiEvent.ReportCommentFailure)
                }
            },
        ).onStart {
            _isLoading.update { true }
        }.onEach {
            _uiEvent.send(BalanceGameUiEvent.ReportCommentSuccess)
        }.onCompletion {
            _isLoading.update { false }
        }.launchIn(viewModelScope)
    }
}
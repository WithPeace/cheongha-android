package com.withpeace.withpeace.feature.balancegame

import com.withpeace.withpeace.core.domain.model.balancegame.BalanceGame
import com.withpeace.withpeace.core.domain.model.balancegame.BalanceGameComment

data class BalanceGameUiModel(
    val gameId: Long,
    val date: String,
    val gameTitle: String,
    val optionA: String,
    val optionB: String,
    val percentageOptionA: Int,
    val percentageOptionB: Int,
    val userChoice: String?,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val totalCount: Int,
    val commentCount: Int,
    val comments: List<Comment>,
)

data class Comment(
    val commentId: Long,
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String,
    val content: String,
    val userChoice: String?,
    val createDate: String,
)

fun BalanceGame.toUIModel(): BalanceGameUiModel {
    return BalanceGameUiModel(
        gameId = id,
        date = date,
        gameTitle = title,
        optionA = optionA,
        optionB = optionB,
        percentageOptionA = getAPercentage(),
        percentageOptionB = getBPercentage(),
        userChoice = userChoice,
        hasNext = hasNext,
        hasPrevious = hasPrevious,
        totalCount = (optionACount + optionBCount).toInt(),
        commentCount = comments.size,
        comments = comments.map { it.toUiModel() },
    )
}

fun BalanceGameComment.toUiModel(): Comment {
    return Comment(
        commentId = id,
        userId = userId,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        content = content,
        userChoice = userChoice,
        createDate = createDate,
    )
}
package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.balancegame.BalanceGame
import com.withpeace.withpeace.core.domain.model.balancegame.BalanceGameComment
import com.withpeace.withpeace.core.network.di.response.BalanceGameCommentEntity
import com.withpeace.withpeace.core.network.di.response.GameEntity

fun GameEntity.toDomain(): BalanceGame {
    return BalanceGame(
        id = this.gameId,
        date = date,
        title = title,
        optionA = optionA,
        optionB = optionB,
        userChoice = userChoice,
        isActive = isActive,
        optionACount = optionACount,
        optionBCount = optionBCount,
        hasPrevious = hasPrevious,
        hasNext = hasNext,
        comments = comments.map { it.toDomain() },
    )
}

fun BalanceGameCommentEntity.toDomain(): BalanceGameComment {
    return BalanceGameComment(
        id = this.commentId,
        userId = this.userId,
        nickname = this.nickname,
        profileImageUrl = this.profileImageUrl,
        content = this.content,
        userChoice = this.userChoice,
        createDate = this.createDate,
    )
}
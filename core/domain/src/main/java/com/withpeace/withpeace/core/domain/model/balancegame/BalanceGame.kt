package com.withpeace.withpeace.core.domain.model.balancegame

import com.withpeace.withpeace.core.domain.model.date.Date
import kotlin.math.roundToInt

data class BalanceGame(
    val id: Long,
    val date: String,
    val title: String,
    val optionA: String,
    val optionB: String,
    val userChoice: String?,
    val isActive: Boolean,
    val optionACount: Long,
    val optionBCount: Long,
    val hasPrevious: Boolean,
    val hasNext: Boolean,
    val comments: List<BalanceGameComment>,
) {
    fun getAPercentage(): Int {
        val totalSum = optionACount.toDouble() + optionBCount.toDouble()
        if(id== 7L) {
            println(totalSum)
        }
        if (totalSum == 0.0) {
            return 0
        }
        return ((optionACount*100.toDouble() / totalSum)).roundToInt()
    }

    fun getBPercentage(): Int {
        if(optionACount + optionBCount == 0L) {
            return 0
        }
        return 100 - getAPercentage()
    }
}

data class BalanceGameComment(
    val id: Long,
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String,
    val content: String,
    val userChoice: String?,
    val createDate: Date,
)

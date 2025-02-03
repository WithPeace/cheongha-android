package com.withpeace.withpeace.core.domain.model.balancegame

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
        if(optionACount + optionBCount == 0L) {
            return 0
        }
        return (optionACount / (optionACount + optionBCount)).toInt()
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
    val createDate: String,
)

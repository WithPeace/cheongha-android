package com.withpeace.withpeace.core.network.di.response

import kotlinx.serialization.Serializable

// GameResponse.kt

@Serializable
data class GameEntity(
    val gameId: Long,
    val date: String,          // "오늘의 밸런스게임" 또는 "n월 n일 밸런스게임"
    val title: String,         // 밸런스 게임 주제
    val optionA: String,       // A 선택지
    val optionB: String,       // B 선택지
    val userChoice: String?,   // "OPTION_A" 또는 "OPTION_B" 또는 null
    val isActive: Boolean,     // 참여 가능 여부 (날짜가 지난 게임인지)
    val optionACount: Long,    // A 선택지 선택한 인원 수
    val optionBCount: Long,    // B 선택지 선택한 인원 수
    val hasPrevious: Boolean,  // 이 게임의 이전 게임 존재 여부
    val hasNext: Boolean,      // 이 게임의 다음 게임 존재 여부
    val comments: List<BalanceGameCommentEntity>, // 없으면 빈 리스트
)

@Serializable
data class BalanceGameCommentEntity(
    val commentId: Long,
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String,
    val content: String,
    val userChoice: String?,   // "OPTION_A" 또는 "OPTION_B" 또는 null
    val createDate: String,
)


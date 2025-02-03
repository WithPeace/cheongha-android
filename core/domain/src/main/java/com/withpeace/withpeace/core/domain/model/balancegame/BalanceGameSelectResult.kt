package com.withpeace.withpeace.core.domain.model.balancegame

/*
* 미사용 클래스
* 밸런스 게임 선택시 결과 값을 동기화 하기 위한 용도
* 자신이 선택한 카운트만 동기화 하는 것으로 진행
*/
data class BalanceGameSelectResult(
    val optionACount: Long,
    val optionBCount: Long,
)

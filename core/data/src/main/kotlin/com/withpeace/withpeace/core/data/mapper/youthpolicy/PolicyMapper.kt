package com.withpeace.withpeace.core.data.mapper.youthpolicy

import com.withpeace.withpeace.core.domain.model.policy.PolicyClassification
import com.withpeace.withpeace.core.domain.model.policy.PolicyRegion
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.network.di.response.policy.PolicyResponse

internal fun PolicyResponse.toDomain(): YouthPolicy {
    return YouthPolicy(
        id = id,
        title = title,
        introduce = introduce,
        region = region.firstOrNull().codeToRegion(), //TODO 추후 변경 필요
        policyClassification = classification.codeToPolicyClassification(),
        ageInfo = ageInfo,
        isBookmarked = isBookmarked,
        applicationPeriodStatus = applicationPeriodStatus,
    )
}

internal fun String?.codeToRegion(): PolicyRegion {
    return PolicyRegion.entries.find { it.toString() == this } ?: PolicyRegion.기타
}

internal fun PolicyRegion.toEnglish(): String {
    return when (this) {
        PolicyRegion.전국 -> "NATIONWIDE"
        PolicyRegion.서울 -> "SEOUL"
        PolicyRegion.부산 -> "BUSAN"
        PolicyRegion.대구 -> "DAEGU"
        PolicyRegion.인천 -> "INCHEON"
        PolicyRegion.광주 -> "GWANGJU"
        PolicyRegion.대전 -> "DAEJEON"
        PolicyRegion.울산 -> "ULSAN"
        PolicyRegion.경기 -> "GYEONGGI"
        PolicyRegion.강원 -> "GANGWON"
        PolicyRegion.충북 -> "CHUNGBUK"
        PolicyRegion.충남 -> "CHUNGNAM"
        PolicyRegion.전북 -> "JEONBUK"
        PolicyRegion.전남 -> "JEONNAM"
        PolicyRegion.경북 -> "GYEONGBUK"
        PolicyRegion.경남 -> "GYEONGNAM"
        PolicyRegion.제주 -> "JEJU"
        PolicyRegion.세종 -> "SEJONG"
        PolicyRegion.기타 -> "ETC"
    }
}

internal fun String?.codeToPolicyClassification(): PolicyClassification {
    return PolicyClassification.entries.find { it.toString() == this } ?: PolicyClassification.ETC
}

internal fun PolicyClassification.toEnglish(): String {
    return when (this) {
        PolicyClassification.JOB -> "JOB"
        PolicyClassification.RESIDENT -> "RESIDENT"
        PolicyClassification.EDUCATION -> "EDUCATION"
        PolicyClassification.WELFARE_AND_CULTURE -> "WELFARE_AND_CULTURE"
        PolicyClassification.PARTICIPATION_AND_RIGHT -> "PARTICIPATION_AND_RIGHT"
        PolicyClassification.ETC -> throw IllegalStateException("정책 분류를 찾을 수 없습니다.")
    }
}
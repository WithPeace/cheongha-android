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
        region = region.codeToRegion(),
        policyClassification = classification.codeToPolicyClassification(),
        ageInfo = ageInfo,
        isBookmarked = isBookmarked,
    )
}

internal fun String?.codeToRegion(): PolicyRegion {
    return PolicyRegion.entries.find { it.toString() == this } ?: PolicyRegion.기타
}

internal fun PolicyRegion.toCode(): String {
    return when (this) {
        PolicyRegion.중앙부처 -> "003001"
        PolicyRegion.서울 -> "003002001"
        PolicyRegion.부산 -> "003002002"
        PolicyRegion.대구 -> "003002003"
        PolicyRegion.인천 -> "003002004"
        PolicyRegion.광주 -> "003002005"
        PolicyRegion.대전 -> "003002006"
        PolicyRegion.울산 -> "003002007"
        PolicyRegion.경기 -> "003002008"
        PolicyRegion.강원 -> "003002009"
        PolicyRegion.충북 -> "003002010"
        PolicyRegion.충남 -> "003002011"
        PolicyRegion.전북 -> "003002012"
        PolicyRegion.전남 -> "003002013"
        PolicyRegion.경북 -> "003002014"
        PolicyRegion.경남 -> "003002015"
        PolicyRegion.제주 -> "003002016"
        PolicyRegion.세종 -> "003002017"
        PolicyRegion.기타 -> throw IllegalStateException("찾을 수 없는 지역입니다.")
    }
}

internal fun String?.codeToPolicyClassification(): PolicyClassification {
    return PolicyClassification.entries.find { it.toString() == this } ?: PolicyClassification.ETC
}

internal fun PolicyClassification.toCode(): String {
    return when (this) {
        PolicyClassification.JOB -> "023010"
        PolicyClassification.RESIDENT -> "023020"
        PolicyClassification.EDUCATION -> "023030"
        PolicyClassification.WELFARE_AND_CULTURE -> "023040"
        PolicyClassification.PARTICIPATION_AND_RIGHT -> "023050"
        PolicyClassification.ETC -> throw IllegalStateException("정책 분류를 찾을 수 없습니다.")
    }
}
package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.policy.PolicyClassification
import com.withpeace.withpeace.core.domain.model.policy.PolicyRegion
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.network.di.response.YouthPolicyEntity

fun YouthPolicyEntity.toDomain(): YouthPolicy {
    return YouthPolicy(
        id = id,
        title = title ?: "",
        introduce = introduce ?: "",
        region = regionCode.toRegion(),
        policyClassification = classification.toPolicyClassification(),
        ageInfo = ageInfo ?: "",
    )
}

private fun String?.toRegion(): PolicyRegion {
    return when(this?.slice(0..8)) {
        "003002001" -> PolicyRegion.서울
        "003002002" -> PolicyRegion.부산
        "003002003" -> PolicyRegion.대구
        "003002004" -> PolicyRegion.인천
        "003002005" -> PolicyRegion.광주
        "003002006" -> PolicyRegion.대전
        "003002007" -> PolicyRegion.울산
        "003002008" -> PolicyRegion.경기
        "003002009" -> PolicyRegion.강원
        "003002010" -> PolicyRegion.충북
        "003002011" -> PolicyRegion.충남
        "003002012" -> PolicyRegion.전북
        "003002013" -> PolicyRegion.전남
        "003002014" -> PolicyRegion.경북
        "003002015" -> PolicyRegion.경남
        "003002016" -> PolicyRegion.제주
        "003002017" -> PolicyRegion.세종
        else -> throw IllegalArgumentException("Unknown region code: $this")
    }
}

private fun String?.toPolicyClassification(): PolicyClassification {
    return when(this) {
        "023010" -> PolicyClassification.JOB
        "023020" -> PolicyClassification.RESIDENT
        "023030" -> PolicyClassification.EDUCATION
        "023040" -> PolicyClassification.WELFARE_AND_CULTURE
        "023050" -> PolicyClassification.PARTICIPATION_AND_RIGHT
        else -> throw IllegalArgumentException("Unknown policy classification code: $this")
    }
}
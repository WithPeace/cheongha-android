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
        region = regionCode.codeToRegion(),
        policyClassification = classification.codeToPolicyClassification(),
        ageInfo = ageInfo ?: "",
        applicationDetails = applicationDetails ?: "",
        residenceAndIncome = residenceAndIncome ?: "",
        education = education ?: "",
        specialization = specialization ?: "",
        additionalNotes = additionalNotes ?: "",
        participationRestrictions = participationRestrictions ?: "",
        applicationProcess = applicationProcess ?: "",
        screeningAndAnnouncement = screeningAndAnnouncement ?: "",
        applicationSite = applicationSite ?: "",
        submissionDocuments = submissionDocuments ?: "",
    )
}

private fun String?.codeToRegion(): PolicyRegion {
    if (this?.slice(0..5) == "003001") {
        return PolicyRegion.중앙부처
    }
    return when (this?.slice(0..8)) {
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
        else -> PolicyRegion.기타
    }
}

fun PolicyRegion.toCode(): String {
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
        PolicyRegion.기타 -> throw IllegalStateException("")
    }
}

fun String?.codeToPolicyClassification(): PolicyClassification {
    return when (this) {
        "023010" -> PolicyClassification.JOB
        "023020" -> PolicyClassification.RESIDENT
        "023030" -> PolicyClassification.EDUCATION
        "023040" -> PolicyClassification.WELFARE_AND_CULTURE
        "023050" -> PolicyClassification.PARTICIPATION_AND_RIGHT
        else -> PolicyClassification.ETC
    }
}

fun PolicyClassification.toCode(): String {
    return when (this) {
        PolicyClassification.JOB -> "023010"
        PolicyClassification.RESIDENT -> "023020"
        PolicyClassification.EDUCATION -> "023030"
        PolicyClassification.WELFARE_AND_CULTURE -> "023040"
        PolicyClassification.PARTICIPATION_AND_RIGHT -> "023050"
        PolicyClassification.ETC -> throw IllegalStateException("")
    }
}
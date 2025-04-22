package com.withpeace.withpeace.core.network.di.response.policy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PolicyDetailResponse(
    val id: String, // 정책 id
    val title: String, // 정책 제목
    val introduce: String, // 정책 소개
    val classification: String, // 정책 분야
    val applicationDetails: String, // 지원 내용
    val applicationPeriodStatus: String, // 신청기간 상태
    val operationPeriod: String, // 운영 기간
    val ageInfo: String, // 연령
    val residence: String, // 거주지
    val income: String, // 소득
    val education: String, // 학력
    val specialization: String, // 특화 분야
    val additionalNotes: String, // 추가 단서 사항
    val participationRestrictions: String, // 참여 제한 대상
    val applicationProcess: String, // 신청 절차
    val screeningAndAnnouncement: String, // 심사 및 발표
    val applicationSite: String, // 신청 사이트
    val submissionDocuments: String, // 제출 서류
    val etc: String, // 기타 유익 정보
    val managingInstitution: String, // 주관 기관
    val operatingOrganization: String, // 운영 기관
    val referenceSite1: String, // 참고 사이트 1
    val referenceSite2: String, // 참고 사이트 2
    @SerialName("isFavorite") val isBookmarked: Boolean // 찜하기 여부
)
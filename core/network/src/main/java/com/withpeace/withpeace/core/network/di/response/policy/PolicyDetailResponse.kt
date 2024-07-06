package com.withpeace.withpeace.core.network.di.response.policy

import kotlinx.serialization.Serializable

@Serializable
data class PolicyDetailResponse(
    val id: String, // 정책 id
    val title: String, // 정책 제목
    val introduce: String, // 정책 소개
    val classification: String, // 정책 분야
    val applicationDetails: String, // 지원 내용
    val ageInfo: String, // 연령
    val residenceAndIncome: String, // 거주지 및 소득
    val education: String, // 학력
    val specialization: String, // 특화 분야
    val additionalNotes: String, // 추가 단서 사항
    val participationRestrictions: String, // 참여 제한 대상
    val applicationProcess: String, // 신청 절차
    val screeningAndAnnouncement: String, // 심사 및 발표
    val applicationSite: String, // 신청 사이트
    val submissionDocuments: String, // 제출 서류
    val additionalUsefulInformation: String, // 기타 유익 정보
    val supervisingAuthority: String, // 주관 기관
    val operatingOrganization: String, // 운영 기관
    val businessRelatedReferenceSite1: String, // 사업관련 참고 사이트 1
    val businessRelatedReferenceSite2: String, // 사업관련 참고 사이트 2
)

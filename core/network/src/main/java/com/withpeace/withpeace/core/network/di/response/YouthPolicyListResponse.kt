package com.withpeace.withpeace.core.network.di.response

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "youthPolicyList")
data class YouthPolicyListResponse(
    @PropertyElement
    val pageIndex: Int,
    @PropertyElement(name = "totalCount")
    val totalDataCount: Int,
    @Element
    val youthPolicyEntity: List<YouthPolicyEntity>,
)

@Xml(name = "youthPolicy")
data class YouthPolicyEntity(
    @PropertyElement(name = "bizId", writeAsCData = true)
    val id: String,
    @PropertyElement(name = "polyBizSjnm", writeAsCData = true) // XML에서는 String 형식을 CData라고 정의함
    val title: String?, // API 데이터를 넣는 상황에, 휴먼에러를 고려하여 nullable 설정
    @PropertyElement(name = "polyItcnCn", writeAsCData = true)
    val introduce: String?,
    @PropertyElement(name = "polyRlmCd", writeAsCData = true)
    val classification: String?,
    @PropertyElement(name = "polyBizSecd")
    val regionCode: String?,
    @PropertyElement(name = "ageInfo", writeAsCData = true)
    val ageInfo: String?,


    @PropertyElement(name = "sporCn", writeAsCData = true)
    val applicationDetails: String?,
    @PropertyElement(name = "prcpCn", writeAsCData = true)
    val residenceAndIncome: String?,
    @PropertyElement(name = "accrRqisCn", writeAsCData = true)
    val education: String?,
    @PropertyElement(name = "splzRlmRqisCn", writeAsCData = true)
    val specialization: String?,
    @PropertyElement(name = "aditRscn", writeAsCData = true)
    val additionalNotes: String?,
    @PropertyElement(name = "prcpLmttTrgtCn", writeAsCData = true)
    val participationRestrictions: String?,
    @PropertyElement(name = "rqutProcCn", writeAsCData = true)
    val applicationProcess: String?,
    @PropertyElement(name = "jdgnPresCn", writeAsCData = true)
    val screeningAndAnnouncement: String?,
    @PropertyElement(name = "rqutUrla", writeAsCData = true)
    val applicationSite: String?,
    @PropertyElement(name = "pstnPaprCn", writeAsCData = true)
    val submissionDocuments: String?
)
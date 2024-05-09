package com.withpeace.withpeace.feature.home.filtersetting.uistate

import com.withpeace.withpeace.core.domain.model.policy.PolicyRegion

enum class RegionUiModel {
    중앙부처, 서울, 부산, 대구, 인천, 광주, 대전, 울산, 경기, 강원, 충북, 충남, 전북, 전남, 경북, 경남, 제주, 세종, 기타
}

fun PolicyRegion.toUiModel(): RegionUiModel {
    return RegionUiModel.entries.find { this.name == it.name } ?: RegionUiModel.기타
}

fun RegionUiModel.toDomain(): PolicyRegion {
    return PolicyRegion.entries.find { this.name == it.name } ?: PolicyRegion.기타
}
package com.withpeace.withpeace.feature.home.uistate

import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.feature.home.uistate.filter.ClassificationUiModel
import com.withpeace.withpeace.feature.home.uistate.filter.RegionUiModel
import com.withpeace.withpeace.feature.home.uistate.filter.toUiModel

data class PolicyFiltersUiModel(
    val classifications: List<ClassificationUiModel> = listOf(),
    val regions: List<RegionUiModel> = listOf(),
)

fun PolicyFilters.toUiModel(): PolicyFiltersUiModel {
    return PolicyFiltersUiModel(
        regions = regions.map { it.toUiModel() },
        classifications = classifications.map {
            it.toUiModel()
        },
    )
}

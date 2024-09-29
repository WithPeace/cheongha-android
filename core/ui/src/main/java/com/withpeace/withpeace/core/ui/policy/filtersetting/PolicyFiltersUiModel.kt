package com.withpeace.withpeace.core.ui.policy.filtersetting

import com.withpeace.withpeace.core.domain.model.policy.PolicyFilters
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.core.ui.policy.toDomain
import com.withpeace.withpeace.core.ui.policy.toUiModel

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

fun PolicyFiltersUiModel.toDomain(): PolicyFilters {
    return PolicyFilters(
        regions = regions.map { it.toDomain() },
        classifications = classifications.map { it.toDomain() },
    )
}
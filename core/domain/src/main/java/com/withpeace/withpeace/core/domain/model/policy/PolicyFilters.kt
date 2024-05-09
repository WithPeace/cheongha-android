package com.withpeace.withpeace.core.domain.model.policy

data class PolicyFilters(
    val regions: List<PolicyRegion> = emptyList(),
    val classifications: List<PolicyClassification> = emptyList(),
) {
    fun addRegion(region: PolicyRegion): PolicyFilters = copy(regions = regions + region)

    fun removeRegion(region: PolicyRegion): PolicyFilters = copy(regions = regions - region)

    fun addClassification(classification: PolicyClassification): PolicyFilters =
        copy(classifications = classifications + classification)

    fun removeClassification(classification: PolicyClassification): PolicyFilters =
        copy(classifications = classifications - classification)

    fun removeAll(): PolicyFilters = copy(regions = emptyList(), classifications = emptyList())
}
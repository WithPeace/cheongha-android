package com.withpeace.withpeace.core.domain.model.policy

data class PolicyFilters(
    val regions: List<PolicyRegion> = emptyList(),
    val classifications: List<PolicyClassification> = emptyList(),
) {
    fun addRegion(region: PolicyRegion): PolicyFilters = copy(regions = regions + region)

    fun removeRegion(region: PolicyRegion): PolicyFilters = copy(regions = regions - region)

    fun updateRegion(region: PolicyRegion): PolicyFilters {
        return if (!regions.contains(region)) addRegion(region)
        else removeRegion(region)
    }

    fun addClassification(classification: PolicyClassification): PolicyFilters =
        copy(classifications = classifications + classification)

    fun removeClassification(classification: PolicyClassification): PolicyFilters =
        copy(classifications = classifications - classification)

    fun updateClassification(classification: PolicyClassification): PolicyFilters {
        return if (!classifications.contains(classification)) addClassification(classification)
        else removeClassification(classification)
    }

    fun removeAll(): PolicyFilters = copy(regions = emptyList(), classifications = emptyList())
}
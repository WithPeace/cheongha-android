package com.withpeace.withpeace.core.domain.model.policy

data class PolicyFilters(
    val regions: List<PolicyRegion> = emptyList(),
    val classifications: List<PolicyClassification> = emptyList(),
) {
    fun updateRegion(region: PolicyRegion): PolicyFilters {
        return if (!regions.contains(region)) addRegion(region)
        else removeRegion(region)
    }

    private fun addRegion(region: PolicyRegion): PolicyFilters = copy(regions = regions + region)

    private fun removeRegion(region: PolicyRegion): PolicyFilters = copy(regions = regions - region)

    fun updateClassification(classification: PolicyClassification): PolicyFilters {
        return if (!classifications.contains(classification)) addClassification(classification)
        else removeClassification(classification)
    }

    private fun addClassification(classification: PolicyClassification): PolicyFilters =
        copy(classifications = classifications + classification)

    private fun removeClassification(classification: PolicyClassification): PolicyFilters =
        copy(classifications = classifications - classification)

    fun removeAll(): PolicyFilters = copy(regions = emptyList(), classifications = emptyList())
}
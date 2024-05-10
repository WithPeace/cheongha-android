package com.withpeace.withpeace.feature.home.filtersetting.uistate

import android.util.Log

data class FilterListUiState(
    val isClassificationExpanded: Boolean = false,
    val isRegionExpanded: Boolean = false,
) {
    private val allClassifications: List<ClassificationUiModel> = ClassificationUiModel.entries
    private val allRegions: List<RegionUiModel> = RegionUiModel.entries

    fun getClassifications(): List<ClassificationUiModel> {
        return if (isClassificationExpanded) allClassifications.dropLast(ETC_COUNT)
        else allClassifications.subList(0, FOLDED_ITEM_COUNT)
    }

    fun getRegions(): List<RegionUiModel> {
        return if (isRegionExpanded) allRegions.dropLast(ETC_COUNT)
        else allRegions.subList(0, FOLDED_ITEM_COUNT)
    }

    companion object {
        private const val FOLDED_ITEM_COUNT = 3
        private const val ETC_COUNT = 1
    }
}
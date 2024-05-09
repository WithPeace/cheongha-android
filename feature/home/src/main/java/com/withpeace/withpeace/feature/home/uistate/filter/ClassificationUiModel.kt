package com.withpeace.withpeace.feature.home.uistate.filter

import androidx.annotation.StringRes
import com.withpeace.withpeace.core.domain.model.policy.PolicyClassification
import com.withpeace.withpeace.feature.home.R

enum class ClassificationUiModel(@StringRes val resId: Int) {
    JOB(R.string.classification_job),
    RESIDENT(R.string.classification_resident),
    EDUCATION(R.string.classification_education),
    WELFARE_AND_CULTURE(R.string.classification_wefare_and_culture),
    PARTICIPATION_AND_RIGHT(R.string.classification_partification_and_right),
    ETC(R.string.classification_etc)
}

fun PolicyClassification.toUiModel(): ClassificationUiModel {
    return ClassificationUiModel.entries.find { it.name == this.name } ?: ClassificationUiModel.ETC
}

fun ClassificationUiModel.toUiModel(): PolicyClassification {
    return PolicyClassification.entries.find { it.name == this.name } ?: PolicyClassification.ETC
}

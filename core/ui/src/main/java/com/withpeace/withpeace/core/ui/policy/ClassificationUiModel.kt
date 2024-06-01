package com.withpeace.withpeace.core.ui.policy

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.withpeace.withpeace.core.domain.model.policy.PolicyClassification
import com.withpeace.withpeace.core.ui.R
import kotlinx.serialization.Serializable

@Serializable
enum class ClassificationUiModel(
    @StringRes val stringResId: Int,
    @DrawableRes val drawableResId: Int,
) {
    JOB(R.string.classification_job, R.drawable.ic_policy_job),
    RESIDENT(R.string.classification_resident, R.drawable.ic_policy_resident),
    EDUCATION(R.string.classification_education, R.drawable.ic_policy_eductaion),
    WELFARE_AND_CULTURE(R.string.classification_wefare_and_culture, R.drawable.ic_policy_welfare_culture),
    PARTICIPATION_AND_RIGHT(R.string.classification_partification_and_right, R.drawable.ic_policy_participation_right),
    ETC(R.string.classification_etc, R.drawable.ic_policy_participation_right),
}

fun PolicyClassification.toUiModel(): ClassificationUiModel {
    return ClassificationUiModel.entries.find { it.name == this.name } ?: ClassificationUiModel.ETC
}

fun ClassificationUiModel.toDomain(): PolicyClassification {
    return PolicyClassification.entries.find { it.name == this.name } ?: PolicyClassification.ETC
}

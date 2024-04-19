package com.withpeace.withpeace.core.ui

import android.content.Context
import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.date.DurationFromNow
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class DateUiModel(
    val date: LocalDateTime,
) {
    val duration: Duration
        get() = Duration.between(date, LocalDateTime.now())

    val durationFromNow: DurationFromNowUiModel
        get() = date.toDurationFromNowUiModel()
}

sealed class DurationFromNowUiModel {
    data object LessThanOneMinute : DurationFromNowUiModel()
    data object OneMinuteToOneHour : DurationFromNowUiModel()
    data object OneHourToOneDay : DurationFromNowUiModel()
    data object OneDayToSevenDay : DurationFromNowUiModel()
    data object SevenDayToOneYear : DurationFromNowUiModel()
    data object OverOneYear : DurationFromNowUiModel()
}

fun Date.toUiModel(): DateUiModel = DateUiModel(
    date = date,
)

fun LocalDateTime.toDurationFromNowUiModel(): DurationFromNowUiModel {
    val date = Date(this)
    return when (date.durationFromNow) {
        is DurationFromNow.LessThanOneMinute -> DurationFromNowUiModel.LessThanOneMinute

        is DurationFromNow.OneDayToSevenDay -> DurationFromNowUiModel.OneDayToSevenDay

        is DurationFromNow.OneHourToOneDay -> DurationFromNowUiModel.OneHourToOneDay

        is DurationFromNow.OneMinuteToOneHour -> DurationFromNowUiModel.OneMinuteToOneHour

        is DurationFromNow.OverOneYear -> DurationFromNowUiModel.OverOneYear

        is DurationFromNow.SevenDayToOneYear -> DurationFromNowUiModel.SevenDayToOneYear
    }
}

fun DateUiModel.toRelativeString(context: Context): String {
    return when (durationFromNow) {
        is DurationFromNowUiModel.LessThanOneMinute -> {
            context.getString(
                R.string.second_format,
                duration.seconds,
            )
        }

        is DurationFromNowUiModel.OneMinuteToOneHour -> context.getString(
            R.string.minute_format,
            duration.toMinutes(),
        )

        is DurationFromNowUiModel.OneHourToOneDay -> context.getString(
            R.string.hour_format,
            duration.toHours(),
        )

        is DurationFromNowUiModel.OneDayToSevenDay -> context.getString(
            R.string.day_format,
            duration.toDays(),
        )

        is DurationFromNowUiModel.SevenDayToOneYear -> date.format(
            DateTimeFormatter.ofPattern(
                DATE_FORMAT,
            ),
        )

        is DurationFromNowUiModel.OverOneYear -> context.getString(
            R.string.years_format,
            duration.toDays() / DAYS_FOR_YEAR,
        )
    }
}

private const val DATE_FORMAT = "MM월 dd일"
private const val DAYS_FOR_YEAR = 365

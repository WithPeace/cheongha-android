package com.withpeace.withpeace.core.ui

import android.content.Context
import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.date.DurationFromNow
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class DateUiModel(
    val durationFromNow: DurationFromNowUiModel,
)

sealed interface DurationFromNowUiModel {
    data class LessThanOneMinute(val duration: Duration) : DurationFromNowUiModel
    data class OneMinuteToOneHour(val duration: Duration) : DurationFromNowUiModel
    data class OneHourToOneDay(val duration: Duration) : DurationFromNowUiModel
    data class OneDayToSevenDay(val duration: Duration) : DurationFromNowUiModel
    data class SevenDayToOneYear(val date: LocalDateTime) : DurationFromNowUiModel
    data class OverOneYear(val duration: Duration) : DurationFromNowUiModel
}

fun Date.toDurationFromNowUiModel(nowDateTime: LocalDateTime): DateUiModel {
    val durationFromNow = durationFromNow(nowDateTime)
    return DateUiModel(
        durationFromNow = when (durationFromNow) {
            is DurationFromNow.LessThanOneMinute -> DurationFromNowUiModel.LessThanOneMinute(durationFromNow.value)

            is DurationFromNow.OneDayToSevenDay -> DurationFromNowUiModel.OneDayToSevenDay(durationFromNow.value)

            is DurationFromNow.OneHourToOneDay -> DurationFromNowUiModel.OneHourToOneDay(durationFromNow.value)

            is DurationFromNow.OneMinuteToOneHour -> DurationFromNowUiModel.OneMinuteToOneHour(durationFromNow.value)

            is DurationFromNow.OverOneYear -> DurationFromNowUiModel.OverOneYear(durationFromNow.value)

            is DurationFromNow.SevenDayToOneYear -> DurationFromNowUiModel.SevenDayToOneYear(this.date)
        },
    )
}

fun DateUiModel.toRelativeString(context: Context): String {
    return when (durationFromNow) {
        is DurationFromNowUiModel.LessThanOneMinute -> {
            context.getString(
                R.string.second_format,
                durationFromNow.duration.seconds,
            )
        }

        is DurationFromNowUiModel.OneMinuteToOneHour -> context.getString(
            R.string.minute_format,
            durationFromNow.duration.toMinutes(),
        )

        is DurationFromNowUiModel.OneHourToOneDay -> context.getString(
            R.string.hour_format,
            durationFromNow.duration.toHours(),
        )

        is DurationFromNowUiModel.OneDayToSevenDay -> context.getString(
            R.string.day_format,
            durationFromNow.duration.toDays(),
        )

        is DurationFromNowUiModel.SevenDayToOneYear -> durationFromNow.date.format(
            DateTimeFormatter.ofPattern(
                DATE_FORMAT,
            ),
        )

        is DurationFromNowUiModel.OverOneYear -> context.getString(
            R.string.years_format,
            durationFromNow.duration.toDays() / DAYS_FOR_YEAR,
        )
    }
}

private const val DATE_FORMAT = "MM월 dd일"
private const val DAYS_FOR_YEAR = 365

package com.withpeace.withpeace.core.ui

import android.content.Context
import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.date.DurationFromNow
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class DateUiModel(
    val date: LocalDateTime,
    val durationFromNow: DurationFromNowUiModel,
)

sealed class DurationFromNowUiModel(
    val duration: Duration,
) {
    class LessThanOneMinute(duration: Duration) : DurationFromNowUiModel(duration)
    class OneMinuteToOneHour(duration: Duration) : DurationFromNowUiModel(duration)
    class OneHourToOneDay(duration: Duration) : DurationFromNowUiModel(duration)
    class OneDayToSevenDay(duration: Duration) : DurationFromNowUiModel(duration)
    class SevenDayToOneYear(duration: Duration) : DurationFromNowUiModel(duration)
    class OverOneYear(duration: Duration) : DurationFromNowUiModel(duration)
}

fun Date.toUiModel(): DateUiModel = DateUiModel(
    date = date,
    durationFromNow = when (durationFromNow) {
        is DurationFromNow.LessThanOneMinute -> DurationFromNowUiModel.LessThanOneMinute(
            durationFromNow.value,
        )

        is DurationFromNow.OneDayToSevenDay -> DurationFromNowUiModel.OneDayToSevenDay(
            durationFromNow.value,
        )

        is DurationFromNow.OneHourToOneDay -> DurationFromNowUiModel.OneHourToOneDay(
            durationFromNow.value,
        )

        is DurationFromNow.OneMinuteToOneHour -> DurationFromNowUiModel.OneMinuteToOneHour(
            durationFromNow.value,
        )

        is DurationFromNow.OverOneYear -> DurationFromNowUiModel.OverOneYear(
            durationFromNow.value,
        )

        is DurationFromNow.SevenDayToOneYear -> DurationFromNowUiModel.SevenDayToOneYear(
            durationFromNow.value,
        )
    },
)

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

        is DurationFromNowUiModel.SevenDayToOneYear -> date.format(
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

private const val DATE_FORMAT = "MM월 DD일"
private const val DAYS_FOR_YEAR = 365

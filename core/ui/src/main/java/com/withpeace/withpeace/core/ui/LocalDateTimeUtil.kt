package com.withpeace.withpeace.core.ui

import android.content.Context
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toRelativeString(context: Context): String {
    val currentTime = LocalDateTime.now()
    val duration = Duration.between(this, currentTime)
    return when {
        duration.isLessThanOneMinute() -> context.getString(
            R.string.second_format,
            duration.seconds,
        )

        duration.isLessThanOneHour() -> context.getString(
            R.string.minute_format,
            duration.toMinutes(),
        )

        duration.isLessThanOneDay() -> context.getString(
            R.string.hour_format,
            duration.toHours(),
        )

        duration.isLessThanSevenDays() -> context.getString(R.string.day_format, duration.toDays())
        else -> format(DateTimeFormatter.ofPattern(DATE_FORMAT))
    }
}

private fun Duration.isLessThanOneMinute() = toMinutes() < 1
private fun Duration.isLessThanOneHour() = toHours() < 1
private fun Duration.isLessThanOneDay() = toDays() < 1
private fun Duration.isLessThanSevenDays() = toDays() < 7

private const val DATE_FORMAT = "MM월 DD일"

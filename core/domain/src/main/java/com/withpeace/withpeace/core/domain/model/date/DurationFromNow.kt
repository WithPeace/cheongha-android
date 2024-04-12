package com.withpeace.withpeace.core.domain.model.date

import java.time.Duration
import java.time.LocalDateTime

sealed class DurationFromNow(
    val value: Duration,
) {
     class LessThanOneMinute(duration: Duration) : DurationFromNow(duration)
     class OneMinuteToOneHour(duration: Duration) : DurationFromNow(duration)
     class OneHourToOneDay(duration: Duration) : DurationFromNow(duration)
     class OneDayToSevenDay(duration: Duration) : DurationFromNow(duration)
     class SevenDayToOneYear(duration: Duration) : DurationFromNow(duration)
     class OverOneYear(duration: Duration) : DurationFromNow(duration)

    companion object {
        fun from(date: LocalDateTime): DurationFromNow {
            val duration = Duration.between(date, LocalDateTime.now())
            return when {
                duration.isOverOneYear() -> OverOneYear(duration)
                duration.isOverWeekDays() -> SevenDayToOneYear(duration)
                duration.isOverOneDay() -> OneDayToSevenDay(duration)
                duration.isOverOneHour() -> OneHourToOneDay(duration)
                duration.isOverOneMinute() -> OneMinuteToOneHour(duration)
                else -> LessThanOneMinute(duration)
            }
        }

        private fun Duration.isOverOneMinute() = toMinutes() >= 1
        private fun Duration.isOverOneHour() = toHours() >= 1
        private fun Duration.isOverOneDay() = toDays() >= 1
        private fun Duration.isOverWeekDays() = toDays() > DAYS_FOR_WEEK
        private fun Duration.isOverOneYear() = toDays() > DAYS_FOR_YEAR

        private const val DAYS_FOR_YEAR = 365
        private const val DAYS_FOR_WEEK = 7
    }
}

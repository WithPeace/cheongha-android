package com.withpeace.withpeace.core.domain.model.date;

import java.time.LocalDateTime

@JvmInline
value class Date(
    val date: LocalDateTime,
) {
    fun durationFromNow(now: LocalDateTime): DurationFromNow {
        return DurationFromNow.from(now, date)
    }
}

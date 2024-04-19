package com.withpeace.withpeace.core.data.mapper

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toLocalDateTime(): LocalDateTime = LocalDateTime.parse(
    this,
    DateTimeFormatter.ofPattern(SERVER_DATE_FORMAT),
)

fun String.toLocalDateTimeForGmt(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)
    val zonedDateTime = ZonedDateTime.parse(this, formatter)
    val seoulZoneId = ZoneId.of("Asia/Seoul")
    return zonedDateTime.withZoneSameInstant(seoulZoneId).toLocalDateTime()
}

private const val SERVER_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss"

package com.withpeace.withpeace.core.data.mapper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDateTime(): LocalDateTime = LocalDateTime.parse(
    this,
    DateTimeFormatter.ofPattern(SERVER_DATE_FORMAT),
)

private const val SERVER_DATE_FORMAT = "yyyy/MM/dd  HH:mm:SS"

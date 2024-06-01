package com.withpeace.withpeace.core.ui.serializable

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

inline fun <reified T> T.toNavigationValue(): String =
    URLEncoder.encode(Json.encodeToString(this), "UTF-8")

inline fun <reified T> String.parseNavigationValue(): T {
    val decodedString = URLDecoder.decode(this, "UTF-8")
    return Json.decodeFromString(decodedString)
}
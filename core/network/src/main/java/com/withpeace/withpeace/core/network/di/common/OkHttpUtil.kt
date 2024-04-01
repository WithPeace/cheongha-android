package com.withpeace.withpeace.core.network.di.common

import okhttp3.ResponseBody
import org.json.JSONObject

fun ResponseBody.getErrorBody(): WithPeaceErrorBody {
    val json = JSONObject(string())
    val errorBody = JSONObject(json.getString("error") ?: "")
    val errorMessage = errorBody.getString("code")
    val errorCode = errorBody.getInt("code")
    return WithPeaceErrorBody(errorCode, errorMessage)
}
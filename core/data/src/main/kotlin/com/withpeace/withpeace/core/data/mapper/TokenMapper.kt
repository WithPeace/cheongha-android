package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.Token
import com.withpeace.withpeace.core.network.di.response.TokenResponse

fun TokenResponse.toDomain(): Token {
    return Token(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}
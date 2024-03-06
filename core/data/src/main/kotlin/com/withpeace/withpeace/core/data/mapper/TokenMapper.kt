package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.AuthToken
import com.withpeace.withpeace.core.network.di.response.TokenResponse

fun TokenResponse.toDomain(): AuthToken {
    return AuthToken(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
}

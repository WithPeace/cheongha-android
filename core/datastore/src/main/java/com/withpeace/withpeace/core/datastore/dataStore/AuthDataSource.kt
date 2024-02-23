package com.withpeace.withpeace.core.datastore.dataStore

import kotlinx.coroutines.flow.Flow

interface AuthDataSource {

    val accessToken: Flow<String?>

    val refreshToken: Flow<String?>

    suspend fun updateRefreshToken(refreshToken: String)

    suspend fun updateAccessToken(accessToken: String)
}
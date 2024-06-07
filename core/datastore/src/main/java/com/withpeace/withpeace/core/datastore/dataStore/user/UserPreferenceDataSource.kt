package com.withpeace.withpeace.core.datastore.dataStore.user

import kotlinx.coroutines.flow.Flow

interface UserPreferenceDataSource {

    val userId: Flow<Long?>

    val userRole: Flow<String?>

    suspend fun updateUserId(userId: Long)

    suspend fun updateUserRole(userRole: String)
    suspend fun removeAll()
}
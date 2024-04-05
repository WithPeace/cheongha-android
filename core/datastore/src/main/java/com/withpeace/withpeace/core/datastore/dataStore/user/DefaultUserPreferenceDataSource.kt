package com.withpeace.withpeace.core.datastore.dataStore.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class DefaultUserPreferenceDataSource @Inject constructor(
    @Named("user") private val dataStore: DataStore<Preferences>,
) : UserPreferenceDataSource {
    override val userId: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }
    override val userRole: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_ROLE]
    }

    override suspend fun updateUserId(userId: Long) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    override suspend fun updateUserRole(userRole: String) {
        dataStore.edit { preferences ->
            preferences[USER_ROLE] = userRole
        }
    }

    override suspend fun removeAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val USER_ID = longPreferencesKey("USER_ID")
        private val USER_ROLE = stringPreferencesKey("USER_ROLE")
    }
}
package com.withpeace.withpeace.core.datastore.dataStore.balancegame

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class DefaultBalanceGameDataStore @Inject constructor(
    @Named("balance_game") private val dataStore: DataStore<Preferences>,
) : BalanceGameDataStore {
    override val isVisited: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[BALANCE_GAME_VISITED] ?: false
    }

    override suspend fun updateVisitedStatus(visited: Boolean) {
        dataStore.edit { preferences ->
            preferences[BALANCE_GAME_VISITED] = visited
        }
    }

    companion object {
        private val BALANCE_GAME_VISITED = booleanPreferencesKey("BALANCE_GAME_VISITED")
    }
}
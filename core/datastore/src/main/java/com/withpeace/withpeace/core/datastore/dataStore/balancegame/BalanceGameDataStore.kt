package com.withpeace.withpeace.core.datastore.dataStore.balancegame

import kotlinx.coroutines.flow.Flow

interface BalanceGameDataStore {

    val isVisited: Flow<Boolean>

    suspend fun updateVisitedStatus(visited: Boolean)
}

//TODO 구현체 설정
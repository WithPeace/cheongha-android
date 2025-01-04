package com.withpeace.withpeace.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface BalanceGameRepository {
    fun isVisited(): Flow<Boolean>
}
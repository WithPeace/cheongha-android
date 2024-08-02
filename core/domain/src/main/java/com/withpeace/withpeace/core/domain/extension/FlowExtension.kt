package com.withpeace.withpeace.core.domain.extension

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow

fun <T, K> Flow<T>.groupBy(getKey: (T) -> K): Flow<Pair<K, Flow<T>>> = flow {
    val storage = mutableMapOf<K, SendChannel<T>>()
    try {
        collect { t ->
            val key = getKey(t)
            storage.getOrPut(key) {
                Channel<T>(32).also { emit(key to it.consumeAsFlow()) }
            }.send(t)
        }
    } finally {
        storage.values.forEach { chan -> chan.close() }
    }
}
package com.cyrillrx.core.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.Volatile

/**
 * Memoizes the result of a suspending [loader].
 *
 * [loader] runs at most once even when [get] is called concurrently from several coroutines:
 * the [Mutex] serializes the load and the double-check skips it once the value is set. This
 * guards the lazy repository caches against the check-then-act race that a multi-threaded
 * dispatcher makes reachable.
 *
 * A failing [loader] is not memoized: the exception propagates to the caller and the next
 * [get] retries the load.
 */
class LazyCache<T : Any>(private val loader: suspend () -> T) {
    private val mutex = Mutex()

    @Volatile
    private var value: T? = null

    suspend fun get(): T =
        value ?: mutex.withLock { value ?: loader().also { value = it } }
}

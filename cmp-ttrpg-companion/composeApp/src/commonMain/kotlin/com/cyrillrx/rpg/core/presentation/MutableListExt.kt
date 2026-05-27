package com.cyrillrx.rpg.core.presentation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

internal fun <T> MutableList<T>.commitAllPending(
    dispatcher: CoroutineDispatcher,
    block: suspend (T) -> Unit,
) {
    val toCommit = toList()
    clear()
    if (toCommit.isEmpty()) return

    CoroutineScope(dispatcher).launch {
        toCommit.forEach { item ->
            try {
                block(item)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Best-effort; ViewModel is cleared so the UI cannot be recovered
            }
        }
    }
}

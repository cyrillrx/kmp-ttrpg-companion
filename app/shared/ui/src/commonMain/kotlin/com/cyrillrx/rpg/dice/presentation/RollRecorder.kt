package com.cyrillrx.rpg.dice.presentation

import com.cyrillrx.rpg.core.presentation.detachedCommitScope
import com.cyrillrx.rpg.dice.domain.RollContext
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import com.cyrillrx.rpg.dice.domain.RollPool
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/** Writes the rolls in the order they were made, even one made as the screen closes; a failed write is dropped. */
class RollRecorder(private val historyRepository: RollHistoryRepository) {

    private val rollsToRecord = Channel<RollToRecord>(Channel.UNLIMITED)

    init {
        detachedCommitScope().launch {
            for (roll in rollsToRecord) {
                val id = recordIgnoringFailure(roll.pool, roll.context) ?: continue

                roll.onRecorded(id)
            }
        }
    }

    fun record(pool: RollPool, context: RollContext? = null, onRecorded: (Long) -> Unit = {}) {
        rollsToRecord.trySend(RollToRecord(pool, context, onRecorded))
    }

    fun close() {
        rollsToRecord.close()
    }

    private suspend fun recordIgnoringFailure(pool: RollPool, context: RollContext?): Long? = try {
        historyRepository.record(pool, context)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        println("WARNING: failed to record the dice roll: $e")
        null
    }

    private class RollToRecord(val pool: RollPool, val context: RollContext?, val onRecorded: (Long) -> Unit)
}

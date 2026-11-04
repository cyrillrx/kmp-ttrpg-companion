package com.cyrillrx.rpg.dice.domain

import kotlinx.coroutines.flow.StateFlow

const val MAX_RECORDED_ROLLS = 500

interface RollHistoryRepository {
    /** The recorded rolls, newest first; may stay empty until [load] has read them. */
    val rolls: StateFlow<List<RecordedRoll>>

    /** Reads the stored rolls into [rolls]; throws when the store cannot be read. */
    suspend fun load()

    /**
     * Keeps [pool] as the newest roll, tagged with [context] when given,
     * forgetting the oldest ones beyond [MAX_RECORDED_ROLLS].
     */
    suspend fun record(pool: RollPool, context: RollContext? = null)

    /** Forgets the roll [id] and every older one, so a roll recorded since is kept. */
    suspend fun clearUpTo(id: Long)
}

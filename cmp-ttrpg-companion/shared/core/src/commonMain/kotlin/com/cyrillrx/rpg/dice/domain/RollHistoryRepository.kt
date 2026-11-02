package com.cyrillrx.rpg.dice.domain

const val MAX_RECORDED_ROLLS = 500

interface RollHistoryRepository {
    /** Keeps [pool] as the newest roll, forgetting the oldest ones beyond [MAX_RECORDED_ROLLS]. */
    suspend fun record(pool: RollPool)

    /** The recorded rolls, newest first. */
    suspend fun getAll(): List<RecordedRoll>
}

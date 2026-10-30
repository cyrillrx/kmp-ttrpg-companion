package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.dice.domain.MAX_RECORDED_ROLLS
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import com.cyrillrx.rpg.dice.domain.RollPool
import kotlin.time.Clock

class RamRollHistoryRepository(private val clock: Clock = Clock.System) : RollHistoryRepository {
    private val rolls = ArrayDeque<RecordedRoll>()
    private var lastId = 0L

    override suspend fun record(pool: RollPool) {
        rolls.addFirst(RecordedRoll(id = ++lastId, pool = pool, rolledAt = clock.now()))
        while (rolls.size > MAX_RECORDED_ROLLS) rolls.removeLast()
    }

    override suspend fun getAll(): List<RecordedRoll> = rolls.toList()
}

package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.dice.domain.MAX_RECORDED_ROLLS
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import com.cyrillrx.rpg.dice.domain.RollPool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock

class RamRollHistoryRepository(private val clock: Clock = Clock.System) : RollHistoryRepository {
    override val rolls: StateFlow<List<RecordedRoll>>
        field = MutableStateFlow(emptyList())

    private var lastId = 0L

    override suspend fun load() = Unit

    override suspend fun record(pool: RollPool) {
        val recorded = RecordedRoll(id = ++lastId, pool = pool, rolledAt = clock.now())
        rolls.update { (listOf(recorded) + it).take(MAX_RECORDED_ROLLS) }
    }

    override suspend fun clearUpTo(id: Long) {
        rolls.update { current -> current.filter { it.id > id } }
    }
}

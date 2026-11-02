package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import com.cyrillrx.rpg.dice.domain.RollPool
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class SQLDelightRollHistoryRepository(
    databaseDriverFactory: DatabaseDriverFactory,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val clock: Clock = Clock.System,
) : RollHistoryRepository {
    private val database = Database(databaseDriverFactory)

    override val rolls: StateFlow<List<RecordedRoll>>
        field = MutableStateFlow(emptyList())

    private val writeLock = Mutex()

    override suspend fun load() {
        writeLock.withLock { withContext(ioDispatcher) { rolls.value = database.getAllDiceRolls() } }
    }

    override suspend fun record(pool: RollPool) {
        val now = clock.now().toEpochMilliseconds()
        writeLock.withLock {
            withContext(ioDispatcher) {
                database.recordDiceRoll(pool, rolledAt = now)
                rolls.value = database.getAllDiceRolls()
            }
        }
    }

    override suspend fun clearUpTo(id: Long) {
        writeLock.withLock {
            withContext(ioDispatcher) {
                database.clearDiceRollsUpTo(id)
                rolls.value = database.getAllDiceRolls()
            }
        }
    }
}

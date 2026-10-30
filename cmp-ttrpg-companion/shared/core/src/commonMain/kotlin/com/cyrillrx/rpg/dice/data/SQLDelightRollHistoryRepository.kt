package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import com.cyrillrx.rpg.dice.domain.RollPool
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class SQLDelightRollHistoryRepository(
    databaseDriverFactory: DatabaseDriverFactory,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val clock: Clock = Clock.System,
) : RollHistoryRepository {
    private val database = Database(databaseDriverFactory)

    override suspend fun record(pool: RollPool) {
        val now = clock.now().toEpochMilliseconds()
        withContext(ioDispatcher) { database.recordDiceRoll(pool, rolledAt = now) }
    }

    override suspend fun getAll(): List<RecordedRoll> = withContext(ioDispatcher) { database.getAllDiceRolls() }
}

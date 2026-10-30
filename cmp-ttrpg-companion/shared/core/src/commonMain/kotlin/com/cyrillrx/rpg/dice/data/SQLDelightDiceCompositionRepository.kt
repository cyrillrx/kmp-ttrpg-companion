package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.dice.domain.DiceComposition
import com.cyrillrx.rpg.dice.domain.DiceCompositionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SQLDelightDiceCompositionRepository(
    databaseDriverFactory: DatabaseDriverFactory,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DiceCompositionRepository {
    private val database = Database(databaseDriverFactory)

    override suspend fun load(): DiceComposition = withContext(ioDispatcher) {
        database.getDiceComposition() ?: DiceComposition()
    }

    override suspend fun save(composition: DiceComposition) {
        withContext(ioDispatcher) { database.saveDiceComposition(composition) }
    }
}

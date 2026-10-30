package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.dice.domain.DiceRollerDraft
import com.cyrillrx.rpg.dice.domain.DiceRollerDraftRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SQLDelightDiceRollerDraftRepository(
    databaseDriverFactory: DatabaseDriverFactory,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DiceRollerDraftRepository {
    private val database = Database(databaseDriverFactory)

    override suspend fun load(): DiceRollerDraft = withContext(ioDispatcher) {
        database.getDiceRollerDraft() ?: DiceRollerDraft()
    }

    override suspend fun save(draft: DiceRollerDraft) {
        withContext(ioDispatcher) { database.saveDiceRollerDraft(draft) }
    }
}

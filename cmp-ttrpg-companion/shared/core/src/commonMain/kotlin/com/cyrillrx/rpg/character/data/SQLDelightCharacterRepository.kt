package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SQLDelightCharacterRepository(
    databaseDriverFactory: DatabaseDriverFactory,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CharacterRepository {

    private val database = Database(databaseDriverFactory)

    override suspend fun getAll(filter: CharacterFilter?): List<Character> = withContext(ioDispatcher) {
        val characters = database.getAllCharacters()
        if (filter == null) characters else characters.filter { it.matches(filter) }
    }

    override suspend fun get(id: String): Character? = withContext(ioDispatcher) { database.getCharacter(id) }

    override suspend fun getByIds(ids: List<String>): List<Character> =
        withContext(ioDispatcher) { database.getCharactersByIds(ids) }

    override suspend fun save(character: Character) {
        withContext(ioDispatcher) { database.saveCharacter(character) }
    }

    override suspend fun delete(id: String) {
        withContext(ioDispatcher) { database.deleteCharacter(id) }
    }

    private fun Character.matches(filter: CharacterFilter): Boolean =
        filter.query.isBlank() || name.contains(filter.query, ignoreCase = true)
}

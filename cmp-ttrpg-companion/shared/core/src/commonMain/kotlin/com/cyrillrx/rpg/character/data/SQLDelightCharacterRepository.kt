package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory

class SQLDelightCharacterRepository(databaseDriverFactory: DatabaseDriverFactory) : CharacterRepository {

    private val database = Database(databaseDriverFactory)

    override suspend fun getAll(filter: CharacterFilter?): List<Character> {
        val characters = database.getAllCharacters()
        filter ?: return characters
        return characters.filter { it.matches(filter) }
    }

    override suspend fun get(id: String): Character? = database.getCharacter(id)

    override suspend fun getByIds(ids: List<String>): List<Character> =
        database.getCharactersByIds(ids)

    override suspend fun save(character: Character) = database.saveCharacter(character)

    override suspend fun delete(id: String) = database.deleteCharacter(id)

    private fun Character.matches(filter: CharacterFilter): Boolean =
        filter.query.isBlank() || name.contains(filter.query, ignoreCase = true)
}

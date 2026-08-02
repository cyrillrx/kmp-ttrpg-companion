package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.applyFilter
import com.cyrillrx.rpg.core.domain.Stored
import kotlin.time.Clock

class RamCharacterRepository(
    private val clock: Clock = Clock.System,
) : CharacterRepository {

    private val characters = mutableMapOf<String, Stored<Character>>()

    override suspend fun getAll(filter: CharacterFilter?): List<Stored<Character>> =
        characters.values.toList()
            .applyFilter(filter)
            .sortedByDescending { it.updatedAt }

    override suspend fun get(id: String): Character? = characters[id]?.value

    override suspend fun getByIds(ids: List<String>): List<Character> = ids.mapNotNull { characters[it]?.value }

    override suspend fun save(character: Character) {
        characters[character.id] = Stored(value = character, updatedAt = clock.now())
    }

    override suspend fun delete(id: String) {
        characters.remove(id)
    }
}

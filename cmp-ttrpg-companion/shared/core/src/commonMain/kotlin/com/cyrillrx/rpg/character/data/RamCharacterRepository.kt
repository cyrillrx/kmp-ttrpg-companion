package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.applyFilter

class RamCharacterRepository : CharacterRepository {

    private val characters = mutableMapOf<String, Character>()

    override suspend fun getAll(filter: CharacterFilter?): List<Character> {
        val allCharacters = characters.values.toList()
        return allCharacters.applyFilter(filter)
    }

    override suspend fun get(id: String): Character? = characters[id]

    override suspend fun save(character: Character) {
        characters[character.id] = character
    }

    override suspend fun delete(id: String) {
        characters.remove(id)
    }
}

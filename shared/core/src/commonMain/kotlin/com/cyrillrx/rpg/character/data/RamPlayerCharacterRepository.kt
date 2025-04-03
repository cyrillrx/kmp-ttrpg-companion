package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.character.domain.PlayerCharacterFilter
import com.cyrillrx.rpg.character.domain.PlayerCharacterRepository

class RamPlayerCharacterRepository : PlayerCharacterRepository {

    private val playerCharacters = mutableMapOf<String, PlayerCharacter>()

    override suspend fun getAll(filter: PlayerCharacterFilter?): List<PlayerCharacter> {
        val allPlayerCharacters = playerCharacters.values.toList()

        filter ?: return allPlayerCharacters

        return allPlayerCharacters.filter(filter::matches)
    }

    override suspend fun get(id: String): PlayerCharacter? = playerCharacters[id]

    override suspend fun save(playerCharacter: PlayerCharacter) {
        playerCharacters[playerCharacter.id] = playerCharacter
    }

    override suspend fun delete(id: String) {
        playerCharacters.remove(id)
    }
}

package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.character.domain.PlayerCharacterRepository

class RamPlayerCharacterRepository : PlayerCharacterRepository {

    private val playerCharacters = mutableMapOf<String, PlayerCharacter>()

    override suspend fun getAll(): List<PlayerCharacter> = playerCharacters.values.toList()

    override suspend fun filter(query: String): List<PlayerCharacter> =
        playerCharacters.values.filter { it.name.contains(query) }

    override suspend fun get(id: String): PlayerCharacter? = playerCharacters[id]

    override suspend fun save(playerCharacter: PlayerCharacter) {
        playerCharacters[playerCharacter.id] = playerCharacter
    }

    override suspend fun delete(id: String) {
        playerCharacters.remove(id)
    }
}

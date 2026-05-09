package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.character.domain.PlayerCharacterFilter
import com.cyrillrx.rpg.character.domain.PlayerCharacterRepository
import com.cyrillrx.rpg.character.domain.applyFilter

class RamPlayerCharacterRepository : PlayerCharacterRepository {

    private val playerCharacters = mutableMapOf<String, PlayerCharacter>()

    override suspend fun getAll(filter: PlayerCharacterFilter?): List<PlayerCharacter> {
        val allPlayerCharacters = playerCharacters.values.toList()
        return allPlayerCharacters.applyFilter(filter)
    }

    override suspend fun get(id: String): PlayerCharacter? = playerCharacters[id]

    override suspend fun save(playerCharacter: PlayerCharacter) {
        playerCharacters[playerCharacter.id] = playerCharacter
    }

    override suspend fun delete(id: String) {
        playerCharacters.remove(id)
    }
}

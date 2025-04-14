package com.cyrillrx.rpg.server.character.data

import com.cyrillrx.rpg.server.character.domain.PlayerCharacter
import com.cyrillrx.rpg.server.character.domain.PlayerCharacterRepository
import org.springframework.stereotype.Repository

@Repository
class RamPlayerCharacterRepository : PlayerCharacterRepository {

    private val characters = mutableListOf<PlayerCharacter>().apply {
        add(PlayerCharacter("1", "Aragorn", "1", mapOf(
            "class" to "Ranger",
            "level" to 10,
            "race" to "Human"
        )))
        add(PlayerCharacter("2", "Gandalf", "1", mapOf(
            "class" to "Wizard",
            "level" to 20,
            "race" to "Maiar"
        )))
        add(PlayerCharacter("3", "V", "3", mapOf(
            "clan" to "Toreador",
            "generation" to 10,
            "humanity" to 7
        )))
    }

    override suspend fun getAll(): List<PlayerCharacter> = characters

    override suspend fun get(id: String): PlayerCharacter? = characters.find { it.id == id }

    override suspend fun save(playerCharacter: PlayerCharacter) {
        delete(playerCharacter.id)
        characters.add(playerCharacter)
    }

    override suspend fun delete(id: String) {
        characters.removeAll { it.id == id }
    }
}
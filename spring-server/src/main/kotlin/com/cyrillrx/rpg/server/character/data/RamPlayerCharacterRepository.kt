package com.cyrillrx.rpg.server.character.data

import com.cyrillrx.rpg.server.character.domain.PlayerCharacter
import com.cyrillrx.rpg.server.character.domain.PlayerCharacterRepository
import org.springframework.stereotype.Repository

@Repository
class RamPlayerCharacterRepository : PlayerCharacterRepository {

    private val characters = mutableListOf<PlayerCharacter>().apply {
        val character1 = PlayerCharacter(
            "1",
            "Aragorn",
            mapOf(
                "class" to "Ranger",
                "level" to 10,
                "race" to "Human"
            )
        )
        add(character1)

        val character2 = PlayerCharacter(
            "2",
            "Gandalf",
            mapOf(
                "class" to "Wizard",
                "level" to 20,
                "race" to "Maiar",
            ),
        )
        add(character2)

        val character3 = PlayerCharacter(
            "3",
            "V",
            mapOf(
                "clan" to "Toreador",
                "generation" to 10,
                "humanity" to 7,
            ),
        )
        add(character3)
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
package com.cyrillrx.rpg.character.domain

interface PlayerCharacterRepository {
    suspend fun getAll(filter: PlayerCharacterFilter?): List<PlayerCharacter>
    suspend fun get(id: String): PlayerCharacter?
    suspend fun save(playerCharacter: PlayerCharacter)
    suspend fun delete(id: String)
}

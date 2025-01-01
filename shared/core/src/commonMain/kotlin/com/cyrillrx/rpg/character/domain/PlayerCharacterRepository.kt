package com.cyrillrx.rpg.character.domain

interface PlayerCharacterRepository {
    suspend fun getAll(): List<PlayerCharacter>
    suspend fun filter(query: String): List<PlayerCharacter>
    suspend fun get(id: String): PlayerCharacter?
    suspend fun save(playerCharacter: PlayerCharacter)
    suspend fun delete(id: String)
}

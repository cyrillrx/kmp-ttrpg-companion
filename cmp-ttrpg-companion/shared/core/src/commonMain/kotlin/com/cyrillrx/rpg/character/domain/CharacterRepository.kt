package com.cyrillrx.rpg.character.domain

interface CharacterRepository {
    suspend fun getAll(filter: CharacterFilter?): List<Character>
    suspend fun get(id: String): Character?
    suspend fun getByIds(ids: List<String>): List<Character>
    suspend fun save(character: Character)
    suspend fun delete(id: String)
}

package com.cyrillrx.rpg.server.character

import com.cyrillrx.rpg.server.character.domain.PlayerCharacter
import com.cyrillrx.rpg.server.character.domain.PlayerCharacterRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/character")
class PlayerCharacterController(
    private val characterRepository: PlayerCharacterRepository,
) {
    @GetMapping
    suspend fun getAll(): List<PlayerCharacter> = characterRepository.getAll()

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: String): PlayerCharacter =
        characterRepository.get(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(@RequestBody character: PlayerCharacter): PlayerCharacter {
        val newCharacter = character.copy(id = UUID.randomUUID().toString())
        characterRepository.save(newCharacter)
        return newCharacter
    }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: String,
        @RequestBody character: PlayerCharacter
    ): PlayerCharacter {
        if (characterRepository.get(id) == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        val updatedCharacter = character.copy(id = id)
        characterRepository.save(updatedCharacter)
        return updatedCharacter
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun delete(@PathVariable id: String) {
        if (characterRepository.get(id) == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        characterRepository.delete(id)
    }
}

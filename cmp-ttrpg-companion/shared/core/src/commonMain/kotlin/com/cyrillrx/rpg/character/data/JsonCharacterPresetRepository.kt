package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.LazyCache
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.core.domain.partitionBy
import com.cyrillrx.rpg.character.data.api.ApiCharacter
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.applyFilter
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.core.domain.UNKNOWN_TIMESTAMP
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class JsonCharacterPresetRepository(
    private val fileReader: FileReader,
    private val filePath: String,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CharacterRepository {
    private val cache = LazyCache { loadFromFile().parse() }

    override suspend fun getAll(filter: CharacterFilter?): List<Stored<Character>> = withContext(ioDispatcher) {
        cache.get().applyFilter(filter)
            .map { Stored(value = it, updatedAt = UNKNOWN_TIMESTAMP) }
    }

    override suspend fun get(id: String): Character? =
        withContext(ioDispatcher) { cache.get().firstOrNull { it.id == id } }

    override suspend fun getByIds(ids: List<String>): List<Character> = withContext(ioDispatcher) {
        val all = cache.get().associateBy { it.id }
        ids.mapNotNull { all[it] }
    }

    override suspend fun save(character: Character) = Unit

    override suspend fun delete(id: String) = Unit

    private suspend fun loadFromFile(): List<ApiCharacter> {
        val result = fileReader.readFile(filePath)
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private const val SOURCE = "character preset"

        private fun List<ApiCharacter>.parse(): List<Character> {
            val (imported, errors) = partitionBy { it.toCharacter() }
            errors.forEach { println("WARNING: $SOURCE import error: $it") }
            imported.flatMap { it.warnings }.forEach { println("WARNING: $SOURCE import warning: $it") }
            return imported.map { it.value }
        }
    }
}

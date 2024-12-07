package com.cyrillrx.rpg.spellbook.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.spellbook.data.api.ApiSpell
import com.cyrillrx.rpg.spellbook.domain.SpellRepository

class JsonSpellRepository(private val fileReader: FileReader) : SpellRepository {

    override suspend fun getAll(): List<ApiSpell> {
        return loadFromFile()
    }

    private suspend fun loadFromFile(): List<ApiSpell> {
        val result = fileReader.readFile("files/grimoire.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }
}

package com.cyrillrx.rpg.spellbook.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.rpg.spellbook.data.api.ApiSpell
import com.cyrillrx.rpg.spellbook.domain.SpellRepository

class JsonSpellRepository(private val fileReader: FileReader) : SpellRepository {

    override suspend fun getAll(): List<ApiSpell> {
        val serializedBestiary: String = fileReader.readFile("files/grimoire.json")
        return serializedBestiary.deserialize()
    }
}

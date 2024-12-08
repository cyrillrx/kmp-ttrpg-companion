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

    override suspend fun filter(query: String): List<ApiSpell> {
        return getAll().filter(query)
    }

    private suspend fun loadFromFile(): List<ApiSpell> {
        val result = fileReader.readFile("files/grimoire.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private fun List<ApiSpell>.filter(query: String): ArrayList<ApiSpell> =
            filterTo(ArrayList()) { spell -> spell.matches(query) }

        private fun ApiSpell.matches(query: String): Boolean {
            val lowerCaseQuery = query.trim().lowercase()
            return title.lowercase().contains(lowerCaseQuery) ||
                content.lowercase().contains(lowerCaseQuery) ||
                lowerCaseQuery in getSpellClasses().map { it.lowercase() }
        }
    }
}

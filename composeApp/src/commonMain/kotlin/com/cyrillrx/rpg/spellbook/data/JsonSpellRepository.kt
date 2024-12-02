package com.cyrillrx.rpg.spellbook.data

import com.cyrillrx.rpg.api.spellbook.ApiSpell
import com.cyrillrx.rpg.spellbook.domain.SpellRepository
import com.cyrillrx.utils.deserialize
import org.jetbrains.compose.resources.ExperimentalResourceApi
import rpg_companion.composeapp.generated.resources.Res

class JsonSpellRepository : SpellRepository {

    @OptIn(ExperimentalResourceApi::class)
    override suspend fun getAll(): List<ApiSpell> {
        val bytes = Res.readBytes("files/grimoire.json")
        val serializedBestiary: String = bytes.decodeToString()
        return serializedBestiary.deserialize()
    }
}

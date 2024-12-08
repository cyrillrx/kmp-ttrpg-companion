package com.cyrillrx.rpg.spellbook.domain

import com.cyrillrx.rpg.spellbook.data.api.ApiSpell

interface SpellRepository {
    suspend fun getAll(): List<ApiSpell>
    suspend fun filter(query: String): List<ApiSpell>
}

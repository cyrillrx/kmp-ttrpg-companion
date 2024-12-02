package com.cyrillrx.rpg.spellbook.domain

import com.cyrillrx.rpg.api.spellbook.ApiSpell

interface SpellRepository {
    suspend fun getAll(): List<ApiSpell>
}

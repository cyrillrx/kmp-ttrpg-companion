package com.cyrillrx.rpg.dnd.spellbook

import com.cyrillrx.rpg.api.spellbook.ApiSpell

object SpellStore {
    private val spells = ArrayList<ApiSpell>()
    val savedSpells = ArrayList<ApiSpell>()

    fun init(spells: List<ApiSpell>) {
        this.spells.apply {
            clear()
            addAll(spells)
        }
    }

    fun save(spell: ApiSpell) {
        savedSpells.add(spell)
    }
}

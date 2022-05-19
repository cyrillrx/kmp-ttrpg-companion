package com.cyrillrx.rpg.dnd.spellbook

import com.cyrillrx.rpg.api.spellbook.Spell

object SpellStore {
    private val spells = ArrayList<Spell> ()
    val savedSpells  = ArrayList<Spell> ()

    fun init(spells : List<Spell>) {
        this.spells.apply {
            clear()
            addAll(spells)
        }
    }

    fun save(spell : Spell) {
        savedSpells.add(spell)
    }
}

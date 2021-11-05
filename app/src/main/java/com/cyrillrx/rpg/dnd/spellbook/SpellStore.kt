package com.cyrillrx.rpg.dnd.spellbook

import com.cyrillrx.rpg.api.spellbook.Spell

/**
 * @author Cyril Leroux
 *         Created on 04/11/2021.
 */
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
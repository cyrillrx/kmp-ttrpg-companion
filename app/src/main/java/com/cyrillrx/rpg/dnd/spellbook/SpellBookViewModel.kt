package com.cyrillrx.rpg.dnd.spellbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cyrillrx.rpg.api.spellbook.Spell
import java.util.Locale

class SpellBookViewModel : ViewModel() {

    var loading by mutableStateOf(false)
        private set
    var query by mutableStateOf("")
        private set
    var displayedSpells = mutableStateListOf<Spell>()
        private set
    var savedSpells = mutableStateListOf<Spell>()
        private set
    var savedSpellsOnly by mutableStateOf(false)
        private set

    private val locale by lazy { Locale.ROOT }
    private var initialSpells: List<Spell> = ArrayList()

    fun init(initialSpells: List<Spell>) {
        this.initialSpells = initialSpells
        updateData()
    }

    fun applyFilter(query: String) {
        this.query = query
        updateData()
    }

    fun onDisplaySavedOnlyClicked(checked: Boolean) {
        this.savedSpellsOnly = checked
        updateData()
    }

    fun onSaveSpell(spell: Spell) {
        if (savedSpells.contains(spell)) {
            savedSpells.remove(spell)
        } else {
            savedSpells.add(spell)
        }
        updateData()
    }

    private fun List<Spell>.filter(query: String): ArrayList<Spell> =
        filterTo(ArrayList()) { spell -> spell.matches(query) }

    private fun Spell.matches(query: String): Boolean {
        val lowerCaseQuery = query.trim().lowercase(locale)
        return title.lowercase(locale).contains(lowerCaseQuery) ||
                content.lowercase(locale).contains(lowerCaseQuery) ||
                lowerCaseQuery in getSpellClasses().map { it.lowercase(locale) }
    }

    private fun updateData() {
        val spellList = if (savedSpellsOnly) savedSpells else initialSpells

        loading = true

        displayedSpells.clear()
        displayedSpells.addAll(spellList.filter(query))

        loading = false
    }
}

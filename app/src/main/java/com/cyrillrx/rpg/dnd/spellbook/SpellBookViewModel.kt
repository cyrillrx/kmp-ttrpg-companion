package com.cyrillrx.rpg.dnd.spellbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cyrillrx.rpg.api.spellbook.Spell
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Cyril Leroux
 *         Created on 29/10/2021.
 */
class SpellBookViewModel : ViewModel() {

    var loading by mutableStateOf(false)

    var query by mutableStateOf("")

    var spells = mutableStateListOf<Spell>()
        private set

    private val locale by lazy { Locale.ROOT }
    private var initialSpells: List<Spell> = ArrayList()

    fun init(initialSpells: List<Spell>) {
        this.initialSpells = initialSpells
        updateData(initialSpells)
    }

    fun applyFilter(query: String) {
        this.query = query
        updateData(initialSpells.filter(query))
    }

    private fun List<Spell>.filter(query: String): ArrayList<Spell> = filterTo(ArrayList()) { spell -> spell.filter(query) }

    private fun Spell.filter(query: String): Boolean {
        val lowerCaseQuery = query.trim().lowercase(locale)
        return title.lowercase(locale).contains(lowerCaseQuery) ||
                content.lowercase(locale).contains(lowerCaseQuery) ||
                lowerCaseQuery in getSpellClasses().map { it.lowercase(locale) }
    }

    private fun updateData(spellList: List<Spell>) {
        loading = true

        spells.clear()
        spells.addAll(spellList)

        loading = false
    }
}

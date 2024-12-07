package com.cyrillrx.rpg.spellbook.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.api.spellbook.ApiSpell
import com.cyrillrx.rpg.spellbook.domain.SpellRepository
import kotlinx.coroutines.launch

class SpellBookViewModel(private val repository: SpellRepository) : ViewModel() {

    private var loading by mutableStateOf(false)

    var query by mutableStateOf("")
        private set
    var displayedSpells = mutableStateListOf<ApiSpell>()
        private set
    var savedSpells = mutableStateListOf<ApiSpell>()
        private set
    var savedSpellsOnly by mutableStateOf(false)
        private set

    private var initialSpells: List<ApiSpell> = ArrayList()

    init {
        viewModelScope.launch {
            initialSpells = repository.getAll()
            updateData()
            loading = false
        }
    }

    fun applyFilter(query: String) {
        this.query = query
        updateData()
    }

    fun onDisplaySavedOnlyClicked(checked: Boolean) {
        this.savedSpellsOnly = checked
        updateData()
    }

    fun onSaveSpell(spell: ApiSpell) {
        if (savedSpells.contains(spell)) {
            savedSpells.remove(spell)
        } else {
            savedSpells.add(spell)
        }
        updateData()
    }

    private fun List<ApiSpell>.filter(query: String): ArrayList<ApiSpell> =
        filterTo(ArrayList()) { spell -> spell.matches(query) }

    private fun ApiSpell.matches(query: String): Boolean {
        val lowerCaseQuery = query.trim().lowercase()
        return title.lowercase().contains(lowerCaseQuery) ||
            content.lowercase().contains(lowerCaseQuery) ||
            lowerCaseQuery in getSpellClasses().map { it.lowercase() }
    }

    private fun updateData() {
        val spellList = if (savedSpellsOnly) savedSpells else initialSpells

        loading = true

        displayedSpells.clear()
        displayedSpells.addAll(spellList.filter(query))

        loading = false
    }
}

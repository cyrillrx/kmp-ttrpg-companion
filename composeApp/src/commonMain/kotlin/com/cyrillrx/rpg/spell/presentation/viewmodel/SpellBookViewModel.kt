package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellListState
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpellBookViewModel(
    private val router: SpellRouter,
    private val repository: SpellRepository,
) : ViewModel() {

    private var updateJob: Job? = null
    private val _state: MutableStateFlow<SpellListState> = MutableStateFlow(
        SpellListState(searchQuery = "", body = SpellListState.Body.Loading),
    )
    val state: StateFlow<SpellListState> = _state.asStateFlow()

    init {
        onSearchQueryChanged(query = "")
    }

    fun onSearchQueryChanged(query: String) {
        updateJob?.cancel()
        updateJob = viewModelScope.launch { updateData(query) }
    }

    fun onSpellClicked(spell: Spell) {
        router.openSpellDetail(spell)
    }

    fun onSpellSaved(spell: Spell) {
        // TODO
//        val savedSpells = _state.value.savedSpells.toMutableList()
//        if (savedSpells.contains(action.spell)) {
//            savedSpells.remove(action.spell)
//        } else {
//            savedSpells.add(action.spell)
//        }
//        _state.update { it.copy(savedSpells = savedSpells) }
    }

    private suspend fun updateData(query: String) {
        _state.update {
            SpellListState(searchQuery = query, body = SpellListState.Body.Loading)
        }

        try {
            val spells = if (query.isBlank()) repository.getAll() else repository.filter(query)
            if (spells.isEmpty()) {
                _state.update { _state.value.copy(body = SpellListState.Body.Empty) }
            } else {
                _state.update { _state.value.copy(body = SpellListState.Body.WithData(spells)) }
            }
        } catch (e: Exception) {
            _state.update { _state.value.copy(body = SpellListState.Body.Error(errorMessage = "Error while loading")) }
        }
    }
}

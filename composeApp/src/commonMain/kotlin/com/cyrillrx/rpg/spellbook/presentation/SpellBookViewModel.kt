package com.cyrillrx.rpg.spellbook.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.spellbook.domain.SpellRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpellBookViewModel(private val repository: SpellRepository) : ViewModel() {

    private val _state: MutableStateFlow<SpellListState> = MutableStateFlow(SpellListState.Empty(searchQuery = ""))
    val state: StateFlow<SpellListState> = _state.asStateFlow()

    init {
        viewModelScope.launch { updateData() }
    }

    fun onAction(action: SpellListAction) {
        when (action) {
            is SpellListAction.OnSearchQueryChanged -> {
                viewModelScope.launch { updateData(action.query) }
            }

            is SpellListAction.OnSpellClicked -> {
            }

            is SpellListAction.OnSaveSpellClicked -> {
//                val savedSpells = _state.value.savedSpells.toMutableList()
//                if (savedSpells.contains(action.spell)) {
//                    savedSpells.remove(action.spell)
//                } else {
//                    savedSpells.add(action.spell)
//                }
//                _state.update { it.copy(savedSpells = savedSpells) }
            }
        }
    }

    private suspend fun updateData(query: String = "") {
        _state.update { SpellListState.Loading(searchQuery = query) }

        try {
            val items = if (query.isBlank()) repository.getAll() else repository.filter(query)
            if (items.isEmpty()) {
                _state.update { SpellListState.Empty(searchQuery = query) }
            } else {
                _state.update { SpellListState.WithData(searchQuery = query, searchResults = items) }
            }
        } catch (e: Exception) {
            _state.update { SpellListState.Error(searchQuery = query, errorMessage = "Error while loading") }
        }
    }
}

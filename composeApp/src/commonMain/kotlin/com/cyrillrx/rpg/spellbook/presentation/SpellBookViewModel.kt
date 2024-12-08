package com.cyrillrx.rpg.spellbook.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.spellbook.domain.SpellRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpellBookViewModel(private val repository: SpellRepository) : ViewModel() {

    private val _state = MutableStateFlow(SpellListState())
    val state: StateFlow<SpellListState> = _state

    init {
        viewModelScope.launch { updateData() }
    }

    fun onAction(action: SpellListAction) {
        when (action) {
            is SpellListAction.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query) }
                viewModelScope.launch { updateData(action.query) }
            }

            is SpellListAction.OnSpellClicked -> {
            }

            is SpellListAction.OnSaveSpellClicked -> {
                val savedSpells = _state.value.savedSpells.toMutableList()
                if (savedSpells.contains(action.spell)) {
                    savedSpells.remove(action.spell)
                } else {
                    savedSpells.add(action.spell)
                }
                _state.update { it.copy(savedSpells = savedSpells) }
            }
        }
    }

    private suspend fun updateData(query: String? = null) {
        _state.update { it.copy(isLoading = true) }

        try {
            _state.update { it.copy(searchResults = emptyList()) }
            val items = if (query == null) repository.getAll() else repository.filter(query)
            _state.update { it.copy(searchResults = items) }

            _state.update { it.copy(errorMessage = null) }
        } catch (e: Exception) {
            _state.update { it.copy(errorMessage = "Error while loading") }
        }

        _state.update { it.copy(isLoading = false) }
    }
}

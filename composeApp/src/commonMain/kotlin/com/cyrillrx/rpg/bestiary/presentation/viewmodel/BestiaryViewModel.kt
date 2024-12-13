package com.cyrillrx.rpg.bestiary.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.bestiary.domain.BestiaryRepository
import com.cyrillrx.rpg.bestiary.presentation.BestiaryAction
import com.cyrillrx.rpg.bestiary.presentation.BestiaryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BestiaryViewModel(private val repository: BestiaryRepository) : ViewModel() {

    private val _state: MutableStateFlow<BestiaryState> = MutableStateFlow(BestiaryState.Empty(searchQuery = ""))
    val state: StateFlow<BestiaryState> = _state.asStateFlow()

    init {
        viewModelScope.launch { updateData(query = "") }
    }

    fun onAction(action: BestiaryAction) {
        when (action) {
            is BestiaryAction.OnSearchQueryChanged -> {
                viewModelScope.launch { updateData(action.query) }
            }

            is BestiaryAction.OnItemClicked -> {}
            is BestiaryAction.OnSaveSpellClicked -> {}
        }
    }

    private suspend fun updateData(query: String) {
        _state.update { BestiaryState.Loading(searchQuery = query) }

        try {
            val items = if (query.isBlank()) repository.getAll() else repository.filter(query)
            if (items.isEmpty()) {
                _state.update { BestiaryState.Empty(searchQuery = query) }
            } else {
                _state.update { BestiaryState.WithData(searchQuery = query, searchResults = items) }
            }
        } catch (e: Exception) {
            _state.update { BestiaryState.Error(searchQuery = query, errorMessage = "Error while loading") }
        }
    }
}

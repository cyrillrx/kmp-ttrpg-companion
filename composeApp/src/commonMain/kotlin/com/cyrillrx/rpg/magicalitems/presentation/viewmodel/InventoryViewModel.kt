package com.cyrillrx.rpg.magicalitems.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.magicalitems.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitems.presentation.MagicalItemListAction
import com.cyrillrx.rpg.magicalitems.presentation.MagicalItemListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: MagicalItemRepository) : ViewModel() {

    private val _state: MutableStateFlow<MagicalItemListState> = MutableStateFlow(
        MagicalItemListState.Empty(searchQuery = ""),
    )
    val state: StateFlow<MagicalItemListState> = _state.asStateFlow()

    init {
        viewModelScope.launch { updateData() }
    }

    fun onAction(action: MagicalItemListAction) {
        when (action) {
            is MagicalItemListAction.OnSearchQueryChanged -> {
                viewModelScope.launch { updateData(action.query) }
            }

            is MagicalItemListAction.OnItemClicked -> {}
            is MagicalItemListAction.OnSaveSpellClicked -> {}
        }
    }

    private suspend fun updateData(query: String = "") {
        _state.update { MagicalItemListState.Loading(searchQuery = query) }

        try {
            val items = if (query.isBlank()) repository.getAll() else repository.filter(query)
            if (items.isEmpty()) {
                _state.update { MagicalItemListState.Empty(searchQuery = query) }
            } else {
                _state.update { MagicalItemListState.WithData(searchQuery = query, searchResults = items) }
            }
        } catch (e: Exception) {
            _state.update { MagicalItemListState.Error(searchQuery = query, errorMessage = "Error while loading") }
        }
    }
}

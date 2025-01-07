package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemListState
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRouter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_magical_items

class MagicalItemListViewModel(
    private val router: MagicalItemRouter,
    private val repository: MagicalItemRepository,
) : ViewModel() {

    private var updateJob: Job? = null
    private val _state: MutableStateFlow<MagicalItemListState> = MutableStateFlow(
        MagicalItemListState(searchQuery = "", body = MagicalItemListState.Body.Empty),
    )
    val state: StateFlow<MagicalItemListState> = _state.asStateFlow()

    init {
        onSearchQueryChanged(query = "")
    }

    fun onNavigateUpClicked() {
        router.navigateUp()
    }

    fun onSearchQueryChanged(query: String) {
        updateJob?.cancel()
        updateJob = viewModelScope.launch { updateData(query) }
    }

    fun onItemClicked(magicalItem: MagicalItem) {
        router.openMagicalItemDetail(magicalItem)
    }

    private suspend fun updateData(query: String) {
        _state.update {
            MagicalItemListState(searchQuery = query, body = MagicalItemListState.Body.Loading)
        }
        try {
            val magicalItems = if (query.isBlank()) repository.getAll() else repository.filter(query)
            if (magicalItems.isEmpty()) {
                _state.update { _state.value.copy(body = MagicalItemListState.Body.Empty) }
            } else {
                _state.update { _state.value.copy(body = MagicalItemListState.Body.WithData(searchResults = magicalItems)) }
            }
        } catch (e: Exception) {
            _state.update { _state.value.copy(body = MagicalItemListState.Body.Error(errorMessage = Res.string.error_while_loading_magical_items)) }
        }
    }
}

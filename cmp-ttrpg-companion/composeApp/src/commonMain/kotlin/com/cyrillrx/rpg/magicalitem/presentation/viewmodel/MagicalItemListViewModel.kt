package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.toggled
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemFilter
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
        MagicalItemListState(body = MagicalItemListState.Body.Empty),
    )
    val state: StateFlow<MagicalItemListState> = _state.asStateFlow()

    init {
        refreshData()
    }

    fun onNavigateUpClicked() {
        router.navigateUp()
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(filter = it.filter.copy(query = query)) }
        refreshData()
    }

    fun onItemClicked(magicalItem: MagicalItem) {
        router.openMagicalItemDetail(magicalItem)
    }

    fun onTypeToggled(type: MagicalItem.Type) {
        _state.update {
            val updatedTypes = it.filter.types.toggled(type)
            it.copy(filter = it.filter.copy(types = updatedTypes))
        }
        refreshData()
    }

    fun onRarityToggled(rarity: MagicalItem.Rarity) {
        _state.update {
            val updatedRarities = it.filter.rarities.toggled(rarity)
            it.copy(filter = it.filter.copy(rarities = updatedRarities))
        }
        refreshData()
    }

    fun onResetFilters() {
        _state.update {
            it.copy(filter = MagicalItemFilter(query = it.filter.query))
        }
        refreshData()
    }

    private fun refreshData() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch { updateData() }
    }

    private suspend fun updateData() {
        _state.update { it.copy(body = MagicalItemListState.Body.Loading) }
        try {
            val filter = _state.value.filter
            val magicalItems = repository.getAll(filter)
            if (magicalItems.isEmpty()) {
                _state.update { it.copy(body = MagicalItemListState.Body.Empty) }
            } else {
                _state.update { it.copy(body = MagicalItemListState.Body.WithData(searchResults = magicalItems)) }
            }
        } catch (e: Exception) {
            _state.update { it.copy(body = MagicalItemListState.Body.Error(errorMessage = Res.string.error_while_loading_magical_items)) }
        }
    }
}

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
import kotlin.coroutines.cancellation.CancellationException

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
        updateFilter { it.copy(query = query) }
    }

    fun onItemClicked(magicalItem: MagicalItem) {
        router.openMagicalItemDetail(magicalItem)
    }

    fun onTypeToggled(type: MagicalItem.Type) {
        updateFilter { it.copy(types = it.types.toggled(type)) }
    }

    fun onRarityToggled(rarity: MagicalItem.Rarity) {
        updateFilter { it.copy(rarities = it.rarities.toggled(rarity)) }
    }

    fun onResetFilters() {
        updateFilter { MagicalItemFilter(query = it.query) }
    }

    private fun updateFilter(transform: (MagicalItemFilter) -> MagicalItemFilter) {
        _state.update { it.copy(filter = transform(it.filter)) }
        refreshData()
    }

    private fun refreshData() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch { updateData() }
    }

    private suspend fun updateData() {
        val filter = _state.value.filter
        _state.update { it.copy(body = MagicalItemListState.Body.Loading) }

        try {
            val magicalItems = repository.getAll(filter)
            val body = if (magicalItems.isEmpty()) {
                MagicalItemListState.Body.Empty
            } else {
                MagicalItemListState.Body.WithData(searchResults = magicalItems)
            }
            _state.update { it.copy(body = body) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _state.update { it.copy(body = MagicalItemListState.Body.Error(errorMessage = Res.string.error_while_loading_magical_items)) }
        }
    }
}

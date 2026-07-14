package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.toggled
import com.cyrillrx.rpg.core.presentation.viewmodel.BaseListViewModel
import com.cyrillrx.rpg.core.presentation.viewmodel.SEARCH_DEBOUNCE_MS
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemFilter
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_magical_items
import kotlin.coroutines.cancellation.CancellationException

class MagicalItemListViewModel(
    private val repository: MagicalItemRepository,
) : BaseListViewModel() {

    private var updateJob: Job? = null
    val state: StateFlow<MagicalItemListState>
        field = MutableStateFlow(MagicalItemListState(body = MagicalItemListState.Body.Empty))

    init {
        refreshData()
    }

    fun filterByQuery(query: String) {
        updateFilter(debounce = true) { it.copy(query = query) }
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

    private fun updateFilter(debounce: Boolean = false, transform: (MagicalItemFilter) -> MagicalItemFilter) {
        state.update { it.copy(filter = transform(it.filter)) }
        scrollToTop()
        refreshData(debounce)
    }

    private fun refreshData(debounce: Boolean = false) {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            if (debounce) delay(SEARCH_DEBOUNCE_MS)
            updateData()
        }
    }

    private suspend fun updateData() {
        val filter = state.value.filter
        if (state.value.body !is MagicalItemListState.Body.WithData) {
            state.update { it.copy(body = MagicalItemListState.Body.Loading) }
        }

        try {
            val magicalItems = repository.getAll(filter)
            val body = if (magicalItems.isEmpty()) {
                MagicalItemListState.Body.Empty
            } else {
                MagicalItemListState.Body.WithData(searchResults = magicalItems)
            }
            state.update { it.copy(body = body) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            state.update {
                it.copy(
                    body = MagicalItemListState.Body.Error(errorMessage = Res.string.error_while_loading_magical_items),
                )
            }
        }
    }
}

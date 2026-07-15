package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import com.cyrillrx.rpg.core.domain.toggled
import com.cyrillrx.rpg.core.presentation.viewmodel.SearchableListViewModel
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemFilter
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemListState
import kotlinx.coroutines.flow.update
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_magical_items

class MagicalItemListViewModel(
    private val repository: MagicalItemRepository,
) : SearchableListViewModel<MagicalItemListState, MagicalItemListState.Body>(
        initialState = MagicalItemListState(body = MagicalItemListState.Body.Loading),
    ) {

    init {
        refresh()
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
        mutableState.update { it.copy(filter = transform(it.filter)) }
        refresh(debounce = debounce, resetScroll = true)
    }

    override fun MagicalItemListState.body(): MagicalItemListState.Body = body

    override fun MagicalItemListState.withBody(body: MagicalItemListState.Body): MagicalItemListState =
        copy(body = body)

    override fun loadingBody(): MagicalItemListState.Body = MagicalItemListState.Body.Loading

    override fun errorBody(): MagicalItemListState.Body =
        MagicalItemListState.Body.Error(errorMessage = Res.string.error_while_loading_magical_items)

    override fun showsContent(body: MagicalItemListState.Body): Boolean =
        body is MagicalItemListState.Body.WithData || body is MagicalItemListState.Body.Empty

    override suspend fun loadContent(): MagicalItemListState.Body {
        val magicalItems = repository.getAll(mutableState.value.filter)
        return if (magicalItems.isEmpty()) {
            MagicalItemListState.Body.Empty
        } else {
            MagicalItemListState.Body.WithData(searchResults = magicalItems)
        }
    }
}

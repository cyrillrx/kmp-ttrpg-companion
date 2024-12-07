package com.cyrillrx.rpg.magicalitems.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.magicalitems.domain.MagicalItem
import com.cyrillrx.rpg.magicalitems.domain.MagicalItemRepository
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: MagicalItemRepository) : ViewModel() {

    private var loading by mutableStateOf(false)

    var query by mutableStateOf("")

    var magicalItems = mutableStateListOf<MagicalItem>()
        private set

    private var initialItems: List<MagicalItem> = ArrayList()

    init {
        viewModelScope.launch {
            initialItems = repository.getAll()
            updateData(initialItems)
            loading = false
        }
    }

    fun applyFilter(query: String) {
        this.query = query
        updateData(initialItems.filter(query))
    }

    private fun List<MagicalItem>.filter(query: String): ArrayList<MagicalItem> =
        filterTo(ArrayList()) { spell -> spell.filter(query) }

    private fun MagicalItem.filter(query: String): Boolean {
        val lowerCaseQuery = query.trim().lowercase()
        return title.lowercase().contains(lowerCaseQuery) ||
            subtitle.lowercase().contains(lowerCaseQuery) ||
            description.lowercase().contains(lowerCaseQuery)
    }

    private fun updateData(itemList: List<MagicalItem>) {
        loading = true

        magicalItems.clear()
        magicalItems.addAll(itemList)

        loading = false
    }
}

package com.cyrillrx.rpg.magicalitems.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.magicalitems.domain.MagicalItemRepository
import com.cyrillrx.rpg.models.magicalitems.MagicalItem
import kotlinx.coroutines.launch
import java.util.Locale

class InventoryViewModel(private val repository: MagicalItemRepository) : ViewModel() {

    var loading by mutableStateOf(false)

    var query by mutableStateOf("")

    var magicalItems = mutableStateListOf<MagicalItem>()
        private set

    private val locale by lazy { Locale.ROOT }
    private var initialItems: List<MagicalItem> = ArrayList()

    init {
        viewModelScope.launch {
            initialItems = repository.getAll()
            updateData(initialItems)
            loading = false
        }
    }

    fun init(initialItems: List<MagicalItem>) {
        this.initialItems = initialItems
        updateData(initialItems)
    }

    fun applyFilter(query: String) {
        this.query = query
        updateData(initialItems.filter(query))
    }

    private fun List<MagicalItem>.filter(query: String): ArrayList<MagicalItem> =
        filterTo(ArrayList()) { spell -> spell.filter(query) }

    private fun MagicalItem.filter(query: String): Boolean {
        val lowerCaseQuery = query.trim().lowercase(locale)
        return title.lowercase(locale).contains(lowerCaseQuery) ||
            subtitle.lowercase(locale).contains(lowerCaseQuery) ||
            description.lowercase(locale).contains(lowerCaseQuery)
    }

    private fun updateData(itemList: List<MagicalItem>) {
        loading = true

        magicalItems.clear()
        magicalItems.addAll(itemList)

        loading = false
    }
}

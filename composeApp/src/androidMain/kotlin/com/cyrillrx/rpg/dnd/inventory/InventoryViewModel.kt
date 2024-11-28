package com.cyrillrx.rpg.dnd.inventory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cyrillrx.rpg.api.inventory.ApiMagicalItem
import java.util.Locale

class InventoryViewModel : ViewModel() {

    var loading by mutableStateOf(false)

    var query by mutableStateOf("")

    var magicalItems = mutableStateListOf<ApiMagicalItem>()
        private set

    private val locale by lazy { Locale.ROOT }
    private var initialItems: List<ApiMagicalItem> = ArrayList()

    fun init(initialItems: List<ApiMagicalItem>) {
        this.initialItems = initialItems
        updateData(initialItems)
    }

    fun applyFilter(query: String) {
        this.query = query
        updateData(initialItems.filter(query))
    }

    private fun List<ApiMagicalItem>.filter(query: String): ArrayList<ApiMagicalItem> =
        filterTo(ArrayList()) { spell -> spell.filter(query) }

    private fun ApiMagicalItem.filter(query: String): Boolean {
        val lowerCaseQuery = query.trim().lowercase(locale)
        return title.lowercase(locale).contains(lowerCaseQuery) ||
            subtitle.lowercase(locale).contains(lowerCaseQuery) ||
            description.lowercase(locale).contains(lowerCaseQuery)
    }

    private fun updateData(itemList: List<ApiMagicalItem>) {
        loading = true

        magicalItems.clear()
        magicalItems.addAll(itemList)

        loading = false
    }
}

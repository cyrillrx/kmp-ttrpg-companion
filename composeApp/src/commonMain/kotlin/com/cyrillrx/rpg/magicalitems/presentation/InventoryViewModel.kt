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
    private var error by mutableStateOf(false)

    var query by mutableStateOf("")
        private set

    var magicalItems = mutableStateListOf<MagicalItem>()
        private set

    init {
        viewModelScope.launch { updateData() }
    }

    fun applyFilter(query: String) {
        this.query = query
        viewModelScope.launch { updateData(query) }
    }

    private suspend fun updateData(query: String? = null) {
        loading = true

        try {
            magicalItems.clear()
            val items = if (query == null) repository.getAll() else repository.filter(query)
            magicalItems.addAll(items)
            error = false
        } catch (e: Exception) {
            error = true
        }

        loading = false
    }
}

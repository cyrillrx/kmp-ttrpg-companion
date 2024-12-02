package com.cyrillrx.rpg.bestiary.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.bestiary.domain.BestiaryRepository
import com.cyrillrx.rpg.models.bestiary.Creature
import kotlinx.coroutines.launch
import java.util.Locale

class BestiaryViewModel(private val repository: BestiaryRepository) : ViewModel() {

    var loading by mutableStateOf(false)

    var query by mutableStateOf("")

    var creatures = mutableStateListOf<Creature>()
        private set

    private val locale by lazy { Locale.ROOT }
    private var initialItems: List<Creature> = ArrayList()

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

    private fun List<Creature>.filter(query: String): ArrayList<Creature> =
        filterTo(ArrayList()) { spell -> spell.filter(query) }

    private fun Creature.filter(query: String): Boolean {
        val lowerCaseQuery = query.trim().lowercase(locale)
        return name.lowercase(locale).contains(lowerCaseQuery)
    }

    private fun updateData(itemList: List<Creature>) {
        loading = true

        creatures.clear()
        creatures.addAll(itemList)

        loading = false
    }
}

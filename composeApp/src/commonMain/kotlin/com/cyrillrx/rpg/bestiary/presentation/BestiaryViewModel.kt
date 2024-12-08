package com.cyrillrx.rpg.bestiary.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.bestiary.domain.BestiaryRepository
import com.cyrillrx.rpg.bestiary.domain.Creature
import kotlinx.coroutines.launch

class BestiaryViewModel(private val repository: BestiaryRepository) : ViewModel() {

    private var loading by mutableStateOf(false)
    private var error by mutableStateOf(false)

    var creatures = mutableStateListOf<Creature>()
        private set

    init {
        viewModelScope.launch { updateData() }
    }

    private suspend fun updateData() {
        loading = true

        try {
            creatures.clear()
            creatures.addAll(repository.getAll())
            error = false
        } catch (e: Exception) {
            error = true
        }

        loading = false
    }
}

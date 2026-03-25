package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.reflect.KClass

class CreatureDetailViewModel(
    creatureId: String,
    repository: CreatureRepository,
) : ViewModel() {
    val state: StateFlow<DetailState<Creature>> = flow {
        val item = repository.getById(creatureId)
        val state = if (item == null) DetailState.NotFound else DetailState.Found(item)
        emit(state)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailState.Loading)
}

class CreatureDetailViewModelFactory(
    private val creatureId: String,
    private val repository: CreatureRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return CreatureDetailViewModel(creatureId, repository) as T
    }
}

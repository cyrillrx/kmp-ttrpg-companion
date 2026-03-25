package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
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
    val item: StateFlow<Creature?> = flow { emit(repository.getById(creatureId)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
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

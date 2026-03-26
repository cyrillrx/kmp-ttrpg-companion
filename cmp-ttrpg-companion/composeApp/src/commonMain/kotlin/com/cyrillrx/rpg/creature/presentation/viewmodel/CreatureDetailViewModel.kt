package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class CreatureDetailViewModel(
    creatureId: String,
    repository: CreatureRepository,
) : ViewModel() {
    val state: StateFlow<DetailState<Creature>> = flow {
        val item = repository.getById(creatureId)
        emit(DetailState.of(creatureId, item))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), DetailState.Loading)
}

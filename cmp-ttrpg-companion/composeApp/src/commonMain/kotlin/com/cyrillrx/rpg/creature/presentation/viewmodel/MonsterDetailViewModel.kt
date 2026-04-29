package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class MonsterDetailViewModel(
    monsterId: String,
    repository: MonsterRepository,
) : ViewModel() {
    val state: StateFlow<DetailState<Monster>> = flow {
        val item = repository.getById(monsterId)
        emit(DetailState.of(monsterId, item))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), DetailState.Loading)
}

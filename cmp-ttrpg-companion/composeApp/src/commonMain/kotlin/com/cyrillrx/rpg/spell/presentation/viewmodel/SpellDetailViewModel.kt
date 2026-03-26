package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class SpellDetailViewModel(
    spellId: String,
    repository: SpellRepository,
) : ViewModel() {
    val state: StateFlow<DetailState<Spell>> = flow {
        val item = repository.getById(spellId)
        emit(DetailState.of(spellId, item))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), DetailState.Loading)
}

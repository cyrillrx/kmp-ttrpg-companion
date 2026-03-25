package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.reflect.KClass

class SpellDetailViewModel(
    spellId: String,
    repository: SpellRepository,
) : ViewModel() {
    val item: StateFlow<Spell?> = flow { emit(repository.getById(spellId)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}

class SpellDetailViewModelFactory(
    private val spellId: String,
    private val repository: SpellRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return SpellDetailViewModel(spellId, repository) as T
    }
}

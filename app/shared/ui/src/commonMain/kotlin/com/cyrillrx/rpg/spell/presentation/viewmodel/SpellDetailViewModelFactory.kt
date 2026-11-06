package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.spell.domain.SpellRepository
import kotlin.reflect.KClass

class SpellDetailViewModelFactory(
    private val spellId: String,
    private val repository: SpellRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return SpellDetailViewModel(spellId, repository) as T
    }
}

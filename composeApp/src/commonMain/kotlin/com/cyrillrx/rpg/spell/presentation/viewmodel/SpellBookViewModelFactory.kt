package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import kotlin.reflect.KClass

class SpellBookViewModelFactory(
    private val router: SpellRouter,
    private val repository: SpellRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return SpellBookViewModel(router, repository) as T
    }
}

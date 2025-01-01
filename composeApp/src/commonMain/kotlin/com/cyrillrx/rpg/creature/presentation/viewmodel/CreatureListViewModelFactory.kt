package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.presentation.navigation.CreatureRouter
import kotlin.reflect.KClass

class CreatureListViewModelFactory(
    private val router: CreatureRouter,
    private val repository: CreatureRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return CreatureListViewModel(router, repository) as T
    }
}

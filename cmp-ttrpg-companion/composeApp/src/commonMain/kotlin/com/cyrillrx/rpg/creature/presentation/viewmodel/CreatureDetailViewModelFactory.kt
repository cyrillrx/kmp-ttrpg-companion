package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import kotlin.reflect.KClass

class CreatureDetailViewModelFactory(
    private val creatureId: String,
    private val repository: CreatureRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return CreatureDetailViewModel(creatureId, repository) as T
    }
}

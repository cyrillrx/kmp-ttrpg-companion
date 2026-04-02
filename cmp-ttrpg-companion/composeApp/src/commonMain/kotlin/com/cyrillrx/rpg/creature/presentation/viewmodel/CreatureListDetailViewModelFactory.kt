package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlin.reflect.KClass

class CreatureListDetailViewModelFactory(
    private val listId: String,
    private val userListRepository: UserListRepository,
    private val creatureRepository: CreatureRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return CreatureListDetailViewModel(listId, userListRepository, creatureRepository) as T
    }
}

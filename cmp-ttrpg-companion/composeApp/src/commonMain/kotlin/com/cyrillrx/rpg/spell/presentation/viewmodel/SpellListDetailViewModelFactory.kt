package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlin.reflect.KClass

class SpellListDetailViewModelFactory(
    private val listId: String,
    private val userListRepository: UserListRepository,
    private val spellRepository: SpellRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return SpellListDetailViewModel(listId, userListRepository, spellRepository) as T
    }
}

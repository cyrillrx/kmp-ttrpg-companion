package com.cyrillrx.rpg.userlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlin.reflect.KClass

class AddToListViewModelFactory(
    private val itemId: String,
    private val listType: UserList.Type,
    private val userListRepository: UserListRepository,
    private val spellRepository: SpellRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return AddSpellToListViewModel(itemId, listType, userListRepository, spellRepository) as T
    }
}

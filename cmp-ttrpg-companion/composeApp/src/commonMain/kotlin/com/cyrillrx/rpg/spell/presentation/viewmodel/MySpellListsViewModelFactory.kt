package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlin.reflect.KClass

class MySpellListsViewModelFactory(
    private val router: SpellRouter,
    private val userListRepository: UserListRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return MySpellListsViewModel(router, userListRepository) as T
    }
}

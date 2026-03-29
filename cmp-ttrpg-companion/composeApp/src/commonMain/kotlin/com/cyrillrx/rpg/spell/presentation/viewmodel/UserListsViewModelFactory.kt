package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.navigation.UserListRouter
import kotlin.reflect.KClass

class UserListsViewModelFactory(
    private val router: UserListRouter,
    private val userListRepository: UserListRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return UserListsViewModel(router, userListRepository) as T
    }
}

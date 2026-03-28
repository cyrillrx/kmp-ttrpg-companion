package com.cyrillrx.rpg.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlin.reflect.KClass

class AddToListViewModelFactory(
    private val itemId: String,
    private val listType: UserList.Type,
    private val userListRepository: UserListRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return AddToListViewModel(itemId, listType, userListRepository) as T
    }
}

package com.cyrillrx.rpg.userlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import org.jetbrains.compose.resources.StringResource
import kotlin.reflect.KClass

class AddToListViewModelFactory<T>(
    private val itemId: String,
    private val listType: UserList.Type,
    private val userListRepository: UserListRepository,
    private val entityRepository: EntityRepository<T>,
    private val errorMessage: StringResource,
) : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: KClass<VM>, extras: CreationExtras): VM {
        @Suppress("UNCHECKED_CAST")
        return AddToListViewModel(itemId, listType, userListRepository, entityRepository, errorMessage) as VM
    }
}

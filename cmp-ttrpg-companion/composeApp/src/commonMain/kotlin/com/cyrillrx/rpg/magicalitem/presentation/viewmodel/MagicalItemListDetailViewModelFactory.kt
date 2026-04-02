package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlin.reflect.KClass

class MagicalItemListDetailViewModelFactory(
    private val listId: String,
    private val userListRepository: UserListRepository,
    private val magicalItemRepository: MagicalItemRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return MagicalItemListDetailViewModel(listId, userListRepository, magicalItemRepository) as T
    }
}

package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRouter
import kotlin.reflect.KClass

class MagicalItemListViewModelFactory(
    private val router: MagicalItemRouter,
    private val repository: MagicalItemRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return MagicalItemListViewModel(router, repository) as T
    }
}

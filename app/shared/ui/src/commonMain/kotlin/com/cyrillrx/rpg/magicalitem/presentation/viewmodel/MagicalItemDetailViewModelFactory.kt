package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import kotlin.reflect.KClass

class MagicalItemDetailViewModelFactory(
    private val magicalItemId: String,
    private val repository: MagicalItemRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return MagicalItemDetailViewModel(magicalItemId, repository) as T
    }
}

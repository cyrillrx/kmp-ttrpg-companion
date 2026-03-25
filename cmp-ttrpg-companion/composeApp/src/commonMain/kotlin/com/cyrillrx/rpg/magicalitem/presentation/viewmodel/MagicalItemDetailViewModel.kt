package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.reflect.KClass

class MagicalItemDetailViewModel(
    magicalItemId: String,
    repository: MagicalItemRepository,
) : ViewModel() {
    val item: StateFlow<MagicalItem?> = flow { emit(repository.getById(magicalItemId)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}

class MagicalItemDetailViewModelFactory(
    private val magicalItemId: String,
    private val repository: MagicalItemRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return MagicalItemDetailViewModel(magicalItemId, repository) as T
    }
}

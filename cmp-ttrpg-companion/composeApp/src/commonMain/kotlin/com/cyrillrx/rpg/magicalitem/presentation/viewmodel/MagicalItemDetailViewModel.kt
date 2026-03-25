package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.core.presentation.state.DetailState
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
    val state: StateFlow<DetailState<MagicalItem>> = flow {
        val item = repository.getById(magicalItemId)
        val state = if (item == null) DetailState.NotFound else DetailState.Found(item)
        emit(state)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailState.Loading)
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

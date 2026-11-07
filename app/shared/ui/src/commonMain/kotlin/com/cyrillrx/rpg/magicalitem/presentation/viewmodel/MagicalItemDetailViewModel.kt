package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class MagicalItemDetailViewModel(
    magicalItemId: String,
    repository: MagicalItemRepository,
) : ViewModel() {
    val state: StateFlow<DetailState<MagicalItem>> = flow {
        val item = repository.getById(magicalItemId)
        emit(DetailState.of(magicalItemId, item))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), DetailState.Loading)
}

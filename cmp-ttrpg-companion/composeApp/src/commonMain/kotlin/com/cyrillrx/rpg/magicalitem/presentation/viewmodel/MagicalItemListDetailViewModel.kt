package com.cyrillrx.rpg.magicalitem.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemListDetailState
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_user_list
import kotlin.coroutines.cancellation.CancellationException

class MagicalItemListDetailViewModel(
    private val listId: String,
    private val userListRepository: UserListRepository,
    private val magicalItemRepository: MagicalItemRepository,
) : ViewModel() {

    val state: StateFlow<MagicalItemListDetailState>
        field = MutableStateFlow(MagicalItemListDetailState())

    init {
        loadDetail()
    }

    fun removeMagicalItem(magicalItemId: String) {
        viewModelScope.launch {
            val result = userListRepository.removeFromList(listId, magicalItemId)
            if (result is UserListRepository.Result.Success) {
                loadDetail()
            }
        }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            state.update { it.copy(body = MagicalItemListDetailState.Body.Loading) }

            try {
                val list = userListRepository.get(listId) ?: error("error_while_loading_user_list")
                state.update { it.copy(listName = list.name) }

                val magicalItems = list.itemIds.mapNotNull { magicalItemRepository.getById(it) }
                val body = if (magicalItems.isEmpty()) {
                    MagicalItemListDetailState.Body.EmptyList
                } else {
                    MagicalItemListDetailState.Body.WithData(magicalItems)
                }
                state.update { it.copy(body = body) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update { it.copy(body = MagicalItemListDetailState.Body.Error(errorMessage = Res.string.error_while_loading_user_list)) }
            }
        }
    }
}

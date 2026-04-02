package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.presentation.CreatureListDetailState
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_user_list
import kotlin.coroutines.cancellation.CancellationException

class CreatureListDetailViewModel(
    private val listId: String,
    private val userListRepository: UserListRepository,
    private val creatureRepository: CreatureRepository,
) : ViewModel() {

    val state: StateFlow<CreatureListDetailState>
        field = MutableStateFlow(CreatureListDetailState())

    init {
        loadDetail()
    }

    fun removeCreature(creatureId: String) {
        viewModelScope.launch {
            val result = userListRepository.removeFromList(listId, creatureId)
            if (result is UserListRepository.Result.Success) {
                loadDetail()
            }
        }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            state.update { it.copy(body = CreatureListDetailState.Body.Loading) }

            try {
                val list = userListRepository.get(listId) ?: error("error_while_loading_user_list")
                state.update { it.copy(listName = list.name) }

                val creatures = creatureRepository.getByIds(list.itemIds)
                val body = if (creatures.isEmpty()) {
                    CreatureListDetailState.Body.EmptyList
                } else {
                    CreatureListDetailState.Body.WithData(creatures)
                }
                state.update { it.copy(body = body) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update { it.copy(body = CreatureListDetailState.Body.Error(errorMessage = Res.string.error_while_loading_user_list)) }
            }
        }
    }
}

package com.cyrillrx.rpg.userlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.UserListsState
import com.cyrillrx.rpg.userlist.presentation.navigation.UserListRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_user_lists
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserListsViewModel(
    private val listType: UserList.Type,
    private val router: UserListRouter,
    private val userListRepository: UserListRepository,
) : ViewModel() {

    val state: StateFlow<UserListsState>
        field = MutableStateFlow(UserListsState())

    init {
        loadLists()
    }

    fun onNavigateUpClicked() {
        router.navigateUp()
    }

    @OptIn(ExperimentalUuidApi::class)
    fun createList(name: String) {
        viewModelScope.launch {
            val newList = UserList(
                id = Uuid.random().toString(),
                name = name,
                type = listType,
                itemIds = emptyList(),
            )
            userListRepository.save(newList)
            loadLists()
        }
    }

    fun deleteList(id: String) {
        viewModelScope.launch {
            userListRepository.delete(id)
            loadLists()
        }
    }

    fun openList(list: UserList) {
        router.openUserList(list)
    }

    private fun loadLists() {
        viewModelScope.launch {
            state.update { it.copy(body = UserListsState.Body.Loading) }

            try {
                val lists = userListRepository.getAll(listType)
                val body = if (lists.isEmpty()) {
                    UserListsState.Body.Empty
                } else {
                    UserListsState.Body.WithData(lists)
                }
                state.update { it.copy(body = body) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update { it.copy(body = UserListsState.Body.Error(errorMessage = Res.string.error_while_loading_user_lists)) }
            }
        }
    }
}

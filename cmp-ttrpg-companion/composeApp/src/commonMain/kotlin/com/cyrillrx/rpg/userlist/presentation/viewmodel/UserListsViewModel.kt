package com.cyrillrx.rpg.userlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.UserListsState
import com.cyrillrx.rpg.userlist.presentation.navigation.UserListRouter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val state: StateFlow<UserListsState>
        field = MutableStateFlow(UserListsState())

    val events: SharedFlow<Event>
        field = MutableSharedFlow<Event>()

    data class PendingDeletion(val list: UserList, val index: Int)

    sealed interface Event {
        data class DeletionError(val list: UserList) : Event
    }

    private val pendingDeletions: MutableList<PendingDeletion> = mutableListOf()

    init {
        loadLists()
    }

    override fun onCleared() {
        super.onCleared()

        commitAllPendingDeletions()
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

    fun deleteListOptimistically(list: UserList): PendingDeletion? {
        val currentState = state.value.body as? UserListsState.Body.WithData ?: return null

        val index = currentState.lists.indexOf(list)
        if (index == -1) return null

        val pending = PendingDeletion(list, index)
        pendingDeletions.add(pending)
        val updatedLists = currentState.lists - list
        val newBody = if (updatedLists.isEmpty()) {
            UserListsState.Body.Empty
        } else {
            UserListsState.Body.WithData(updatedLists)
        }
        state.update { it.copy(body = newBody) }
        return pending
    }

    fun undoDeletion(pending: PendingDeletion) {
        if (!pendingDeletions.remove(pending)) return

        val currentLists = when (val body = state.value.body) {
            is UserListsState.Body.WithData -> body.lists
            is UserListsState.Body.Empty -> emptyList()
            else -> return
        }
        val restoredLists = currentLists.toMutableList().apply { add(pending.index.coerceAtMost(size), pending.list) }
        state.update { it.copy(body = UserListsState.Body.WithData(restoredLists)) }
    }

    fun commitDeletion(pending: PendingDeletion) {
        if (!pendingDeletions.remove(pending)) return

        viewModelScope.launch {
            try {
                userListRepository.delete(pending.list.id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                pendingDeletions.add(pending)
                undoDeletion(pending)
                events.emit(Event.DeletionError(pending.list))
            }
        }
    }

    fun openList(list: UserList) {
        router.openUserList(list)
    }

    internal fun commitAllPendingDeletions() {
        val toCommit = pendingDeletions.toList()
        pendingDeletions.clear()
        if (toCommit.isEmpty()) return

        CoroutineScope(ioDispatcher).launch {
            toCommit.forEach { pending ->
                userListRepository.delete(pending.list.id)
            }
        }
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

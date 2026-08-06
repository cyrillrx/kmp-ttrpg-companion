package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.core.presentation.commitAllPending
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.UserCollectionsState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
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

class UserCollectionsViewModel(
    private val listType: UserCollection.Type,
    private val userCollectionRepository: UserCollectionRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val state: StateFlow<UserCollectionsState>
        field = MutableStateFlow(UserCollectionsState())

    val events: SharedFlow<Event>
        field = MutableSharedFlow<Event>()

    data class PendingDeletion(val stored: Stored<UserCollection>, val index: Int)

    sealed interface Event {
        data class DeletionError(val list: UserCollection) : Event
    }

    private val pendingDeletions: MutableList<PendingDeletion> = mutableListOf()
    private var activeJob: Job? = null

    init {
        activeJob = loadLists()
    }

    @OptIn(ExperimentalUuidApi::class)
    fun createList(name: String) {
        viewModelScope.launch {
            val newList = UserCollection(
                id = Uuid.random().toString(),
                name = name,
                type = listType,
                itemIds = emptyList(),
            )
            userCollectionRepository.save(newList)
            activeJob?.cancel()
            activeJob = loadLists()
        }
    }

    fun deleteListOptimistically(stored: Stored<UserCollection>): PendingDeletion? {
        val currentState = state.value.body as? UserCollectionsState.Body.WithData ?: return null

        val index = currentState.lists.indexOf(stored)
        if (index == -1) return null

        val pending = PendingDeletion(stored, index)
        pendingDeletions.add(pending)
        val updatedLists = currentState.lists - stored
        val newBody = if (updatedLists.isEmpty()) {
            UserCollectionsState.Body.Empty
        } else {
            UserCollectionsState.Body.WithData(updatedLists)
        }
        state.update { it.copy(body = newBody) }
        return pending
    }

    fun undoDeletion(pending: PendingDeletion) {
        if (!pendingDeletions.remove(pending)) return

        val currentLists = when (val body = state.value.body) {
            is UserCollectionsState.Body.WithData -> body.lists
            is UserCollectionsState.Body.Empty -> emptyList()
            else -> return
        }
        val restoredLists = currentLists.toMutableList().apply { add(pending.index.coerceAtMost(size), pending.stored) }
        state.update { it.copy(body = UserCollectionsState.Body.WithData(restoredLists)) }
    }

    fun commitDeletion(pending: PendingDeletion) {
        if (!pendingDeletions.remove(pending)) return

        viewModelScope.launch {
            try {
                userCollectionRepository.delete(pending.stored.value.id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                pendingDeletions.add(pending)
                undoDeletion(pending)
                events.emit(Event.DeletionError(pending.stored.value))
            }
        }
    }

    internal fun commitAllPendingDeletions() {
        pendingDeletions.commitAllPending(ioDispatcher) { pending ->
            userCollectionRepository.delete(pending.stored.value.id)
        }
    }

    fun silentRefresh() {
        if (state.value.body is UserCollectionsState.Body.Loading) return
        activeJob?.cancel()
        activeJob = refreshLists()
    }

    private fun refreshLists(): Job =
        viewModelScope.launch {
            try {
                fetchAndUpdateUserCollections()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Keep existing state on refresh failure
            }
        }

    private fun loadLists(): Job =
        viewModelScope.launch {
            state.update { it.copy(body = UserCollectionsState.Body.Loading) }
            try {
                fetchAndUpdateUserCollections()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        body = UserCollectionsState.Body.Error(
                            errorMessage = Res.string.error_while_loading_user_lists,
                        ),
                    )
                }
            }
        }

    private suspend fun fetchAndUpdateUserCollections() {
        val lists = userCollectionRepository.getAll(listType).sortedByDescending { it.updatedAt }
        val body = if (lists.isEmpty()) {
            UserCollectionsState.Body.Empty
        } else {
            UserCollectionsState.Body.WithData(lists)
        }
        state.update { it.copy(body = body) }
    }
}

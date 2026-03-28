package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.spell.presentation.MySpellListsState
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MySpellListsViewModel(
    private val router: SpellRouter,
    private val userListRepository: UserListRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MySpellListsState())
    val state: StateFlow<MySpellListsState> = _state.asStateFlow()

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
                type = UserList.Type.SPELL,
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
        router.openSpellListDetail(list.id)
    }

    private fun loadLists() {
        viewModelScope.launch {
            _state.update { it.copy(body = MySpellListsState.Body.Loading) }
            val lists = userListRepository.getAll(UserList.Type.SPELL)
            val body = if (lists.isEmpty()) MySpellListsState.Body.Empty else MySpellListsState.Body.WithData(lists)
            _state.update { it.copy(body = body) }
        }
    }
}

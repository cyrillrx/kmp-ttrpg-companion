package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellListDetailState
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpellListDetailViewModel(
    private val listId: String,
    private val userListRepository: UserListRepository,
    private val spellRepository: SpellRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SpellListDetailState())
    val state: StateFlow<SpellListDetailState> = _state.asStateFlow()

    init {
        loadDetail()
    }

    fun removeSpell(spellId: String) {
        viewModelScope.launch {
            val list = userListRepository.get(listId) ?: return@launch
            val updated = list.copy(itemIds = list.itemIds - spellId)
            userListRepository.save(updated)
            loadDetail()
        }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _state.update { it.copy(body = SpellListDetailState.Body.Loading) }
            val list = userListRepository.get(listId) ?: return@launch
            _state.update { it.copy(listName = list.name) }
            val spells = list.itemIds.mapNotNull { spellRepository.getById(it) }
            val body = if (spells.isEmpty()) {
                SpellListDetailState.Body.Empty
            } else {
                SpellListDetailState.Body.WithData(spells)
            }
            _state.update { it.copy(body = body) }
        }
    }
}

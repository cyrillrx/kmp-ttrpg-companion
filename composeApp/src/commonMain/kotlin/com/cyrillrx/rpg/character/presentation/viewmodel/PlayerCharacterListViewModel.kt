package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.PlayerCharacterListState
import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.character.domain.PlayerCharacterRepository
import com.cyrillrx.rpg.character.presentation.navigation.PlayerCharacterRouter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_characters

class PlayerCharacterListViewModel(
    private val router: PlayerCharacterRouter,
    private val repository: PlayerCharacterRepository,
) : ViewModel() {

    private var updateJob: Job? = null
    private val _state: MutableStateFlow<PlayerCharacterListState> = MutableStateFlow(
        PlayerCharacterListState(searchQuery = "", body = PlayerCharacterListState.Body.Empty),
    )
    val state: StateFlow<PlayerCharacterListState> = _state.asStateFlow()

    init {
        onSearchQueryChanged(query = "")
    }

    fun onNavigateUpClicked() {
        router.navigateUp()
    }

    fun onSearchQueryChanged(query: String) {
        updateJob?.cancel()
        updateJob = viewModelScope.launch { updateData(query) }
    }

    fun onCharacterClicked(character: PlayerCharacter) {
        router.openPlayerCharacterDetail(character)
    }

    fun onCreateCharacterClicked() {
        router.openCreatePlayerCharacter()
    }

    private suspend fun updateData(query: String) {
        _state.update { PlayerCharacterListState(searchQuery = "", body = PlayerCharacterListState.Body.Loading) }

        try {
            val characters = if (query.isBlank()) repository.getAll() else repository.filter(query)
            if (characters.isEmpty()) {
                _state.update { _state.value.copy(body = PlayerCharacterListState.Body.Empty) }
            } else {
                _state.update { _state.value.copy(body = PlayerCharacterListState.Body.WithData(characters)) }
            }
        } catch (e: Exception) {
            _state.update { _state.value.copy(body = PlayerCharacterListState.Body.Error(errorMessage = Res.string.error_while_loading_characters)) }
        }
    }
}

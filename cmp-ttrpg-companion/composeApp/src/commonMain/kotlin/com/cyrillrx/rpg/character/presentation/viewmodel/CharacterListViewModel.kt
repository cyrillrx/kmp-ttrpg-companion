package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.presentation.CharacterListState
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_characters
import kotlin.coroutines.cancellation.CancellationException

class CharacterListViewModel(
    private val router: CharacterRouter,
    private val repository: CharacterRepository,
) : ViewModel() {

    private var updateJob: Job? = null
    val state: StateFlow<CharacterListState>
        field = MutableStateFlow(CharacterListState(searchQuery = "", body = CharacterListState.Body.Empty))

    init {
        onSearchQueryChanged(query = "")
    }

    fun onSearchQueryChanged(query: String) {
        updateJob?.cancel()
        updateJob = viewModelScope.launch { updateData(query) }
    }

    fun onCharacterClicked(character: Character) {
        router.openCharacterDetail(character)
    }

    fun onCreateCharacterClicked() {
        router.openCreateCharacter()
    }

    private suspend fun updateData(query: String) {
        state.update { CharacterListState(searchQuery = query, body = CharacterListState.Body.Loading) }

        try {
            val filter = CharacterFilter(query = query)
            val characters = repository.getAll(filter)
            if (characters.isEmpty()) {
                state.update { it.copy(body = CharacterListState.Body.Empty) }
            } else {
                state.update { it.copy(body = CharacterListState.Body.WithData(characters)) }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            state.update { it.copy(body = CharacterListState.Body.Error(errorMessage = Res.string.error_while_loading_characters)) }
        }
    }
}

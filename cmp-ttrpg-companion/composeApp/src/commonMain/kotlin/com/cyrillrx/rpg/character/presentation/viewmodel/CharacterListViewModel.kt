package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.presentation.CharacterListState
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
    private var activeJob: Job? = null
    val state: StateFlow<CharacterListState>
        field = MutableStateFlow(CharacterListState(searchQuery = "", body = CharacterListState.Body.Loading))

    init {
        loadCharacters(query = "")
    }

    fun filterByQuery(query: String) {
        activeJob?.cancel()
        activeJob = loadCharacters(query)
    }

    fun silentRefresh() {
        if (state.value.body is CharacterListState.Body.Loading) return
        activeJob?.cancel()
        activeJob = viewModelScope.launch {
            try {
                fetchAndUpdateCharacters(state.value.searchQuery)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Keep existing state on refresh failure
            }
        }
    }

    fun openCharacterDetail(character: Character) {
        router.openCharacterDetail(character)
    }

    fun openCreateCharacter() {
        router.openCreateCharacter()
    }

    fun openPresetGallery() {
        router.openPresetGallery()
    }

    private fun loadCharacters(query: String): Job =
        viewModelScope.launch {
            state.update { CharacterListState(searchQuery = query, body = CharacterListState.Body.Loading) }
            try {
                fetchAndUpdateCharacters(query)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update { it.copy(body = CharacterListState.Body.Error(errorMessage = Res.string.error_while_loading_characters)) }
            }
        }

    private suspend fun fetchAndUpdateCharacters(query: String) {
        val filter = CharacterFilter(query = query)
        val characters = repository.getAll(filter)
        val body = if (characters.isEmpty()) {
            CharacterListState.Body.Empty
        } else {
            CharacterListState.Body.WithData(characters)
        }
        state.update { it.copy(body = body) }
    }
}

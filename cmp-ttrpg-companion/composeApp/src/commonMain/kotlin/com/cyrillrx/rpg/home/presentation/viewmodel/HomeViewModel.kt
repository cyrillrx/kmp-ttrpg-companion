package com.cyrillrx.rpg.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.home.presentation.HomeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_characters
import kotlin.coroutines.cancellation.CancellationException

class HomeViewModel(
    private val repository: CharacterRepository,
) : ViewModel() {

    val state: StateFlow<HomeState>
        field = MutableStateFlow(HomeState(body = HomeState.Body.Loading))

    private var activeJob: Job? = null

    init {
        loadCharacters(showLoading = true)
    }

    fun silentRefresh() {
        if (state.value.body is HomeState.Body.Loading) return
        activeJob?.cancel()
        activeJob = loadCharacters(showLoading = false)
    }

    private fun loadCharacters(showLoading: Boolean): Job =
        viewModelScope.launch {
            if (showLoading) state.update { it.copy(body = HomeState.Body.Loading) }
            try {
                val characters = repository.getAll(filter = null).take(RECENT_LIMIT)
                state.update { it.copy(body = HomeState.Body.WithData(characters)) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (showLoading) {
                    state.update {
                        it.copy(body = HomeState.Body.Error(errorMessage = Res.string.error_while_loading_characters))
                    }
                }
                // On silent refresh failure, keep the existing state.
            }
        }

    companion object {
        private const val RECENT_LIMIT = 2
    }
}

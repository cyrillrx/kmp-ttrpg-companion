package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureFilter
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.presentation.CreatureListState
import com.cyrillrx.rpg.creature.presentation.navigation.CreatureRouter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_creatures

class CreatureListViewModel(
    private val router: CreatureRouter,
    private val repository: CreatureRepository,
) : ViewModel() {

    private var updateJob: Job? = null
    private val _state: MutableStateFlow<CreatureListState> = MutableStateFlow(
        CreatureListState(searchQuery = "", body = CreatureListState.Body.Empty),
    )
    val state: StateFlow<CreatureListState> = _state.asStateFlow()

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

    fun onCreatureClicked(creature: Creature) {
        router.openCreatureDetail(creature)
    }

    private suspend fun updateData(query: String) {
        _state.update {
            CreatureListState(searchQuery = query, body = CreatureListState.Body.Loading)
        }
        try {
            val filter = CreatureFilter(query = query)
            val creatures = repository.getAll(filter)
            if (creatures.isEmpty()) {
                _state.update { _state.value.copy(body = CreatureListState.Body.Empty) }
            } else {
                _state.update { _state.value.copy(body = CreatureListState.Body.WithData(searchResults = creatures)) }
            }
        } catch (e: Exception) {
            _state.update { _state.value.copy(body = CreatureListState.Body.Error(errorMessage = Res.string.error_while_loading_creatures)) }
        }
    }
}

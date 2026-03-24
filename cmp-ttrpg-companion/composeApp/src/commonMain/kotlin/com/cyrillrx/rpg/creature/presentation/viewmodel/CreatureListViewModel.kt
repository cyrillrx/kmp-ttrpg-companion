package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.toggled
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
        CreatureListState(body = CreatureListState.Body.Empty),
    )
    val state: StateFlow<CreatureListState> = _state.asStateFlow()

    init {
        refreshData()
    }

    fun onNavigateUpClicked() {
        router.navigateUp()
    }

    fun onSearchQueryChanged(query: String) {
        updateFilter { it.copy(query = query) }
    }

    fun onCreatureClicked(creature: Creature) {
        router.openCreatureDetail(creature)
    }

    fun onTypeToggled(type: Creature.Type) {
        updateFilter { it.copy(types = it.types.toggled(type)) }
    }

    fun onChallengeRatingToggled(cr: Float) {
        updateFilter { it.copy(challengeRatings = it.challengeRatings.toggled(cr)) }
    }

    fun onResetFilters() {
        updateFilter { CreatureFilter(query = it.query) }
    }

    private fun updateFilter(transform: (CreatureFilter) -> CreatureFilter) {
        _state.update { it.copy(filter = transform(it.filter)) }
        refreshData()
    }

    private fun refreshData() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch { updateData() }
    }

    private suspend fun updateData() {
        val filter = _state.value.filter
        _state.update { it.copy(body = CreatureListState.Body.Loading) }

        try {
            val creatures = repository.getAll(filter)
            val body = if (creatures.isEmpty()) {
                CreatureListState.Body.Empty
            } else {
                CreatureListState.Body.WithData(searchResults = creatures)
            }
            _state.update { it.copy(body = body) }
        } catch (e: Exception) {
            _state.update { it.copy(body = CreatureListState.Body.Error(errorMessage = Res.string.error_while_loading_creatures)) }
        }
    }
}

package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.toggled
import com.cyrillrx.rpg.creature.domain.MonsterFilter
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.presentation.MonsterListState
import com.cyrillrx.rpg.creature.presentation.navigation.MonsterRouter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_monsters
import kotlin.coroutines.cancellation.CancellationException

class MonsterListViewModel(
    private val router: MonsterRouter,
    private val repository: MonsterRepository,
) : ViewModel() {

    private var updateJob: Job? = null
    val state: StateFlow<MonsterListState>
        field = MutableStateFlow(MonsterListState(body = MonsterListState.Body.Empty))

    init {
        refreshData()
    }

    fun onSearchQueryChanged(query: String) {
        updateFilter { it.copy(query = query) }
    }

    fun onMonsterClicked(monster: Monster) {
        router.openDetail(monster.id)
    }

    fun onTypeToggled(type: Monster.Type) {
        updateFilter { it.copy(types = it.types.toggled(type)) }
    }

    fun onChallengeRatingToggled(cr: Float) {
        updateFilter { it.copy(challengeRatings = it.challengeRatings.toggled(cr)) }
    }

    fun onResetFilters() {
        updateFilter { MonsterFilter(query = it.query) }
    }

    private fun updateFilter(transform: (MonsterFilter) -> MonsterFilter) {
        state.update { it.copy(filter = transform(it.filter)) }
        refreshData()
    }

    private fun refreshData() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch { updateData() }
    }

    private suspend fun updateData() {
        val filter = state.value.filter
        state.update { it.copy(body = MonsterListState.Body.Loading) }

        try {
            val monsters = repository.getAll(filter)
            val body = if (monsters.isEmpty()) {
                MonsterListState.Body.Empty
            } else {
                MonsterListState.Body.WithData(searchResults = monsters)
            }
            state.update { it.copy(body = body) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            state.update { it.copy(body = MonsterListState.Body.Error(errorMessage = Res.string.error_while_loading_monsters)) }
        }
    }
}

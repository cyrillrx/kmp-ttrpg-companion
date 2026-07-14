package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.core.domain.toggled
import com.cyrillrx.rpg.core.presentation.viewmodel.BaseListViewModel
import com.cyrillrx.rpg.core.presentation.viewmodel.SEARCH_DEBOUNCE_MS
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.MonsterFilter
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.presentation.MonsterListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_monsters
import kotlin.coroutines.cancellation.CancellationException

class MonsterListViewModel(
    private val repository: MonsterRepository,
) : BaseListViewModel() {

    private var updateJob: Job? = null
    val state: StateFlow<MonsterListState>
        field = MutableStateFlow(MonsterListState(body = MonsterListState.Body.Empty))

    init {
        refreshData()
    }

    fun filterByQuery(query: String) {
        updateFilter(debounce = true) { it.copy(query = query) }
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

    private fun updateFilter(debounce: Boolean = false, transform: (MonsterFilter) -> MonsterFilter) {
        state.update { it.copy(filter = transform(it.filter)) }
        scrollToTop()
        refreshData(debounce)
    }

    private fun refreshData(debounce: Boolean = false) {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            if (debounce) delay(SEARCH_DEBOUNCE_MS)
            updateData()
        }
    }

    private suspend fun updateData() {
        val filter = state.value.filter
        if (state.value.body !is MonsterListState.Body.WithData) {
            state.update { it.copy(body = MonsterListState.Body.Loading) }
        }

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
            state.update {
                it.copy(body = MonsterListState.Body.Error(errorMessage = Res.string.error_while_loading_monsters))
            }
        }
    }
}

package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.core.domain.toggled
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellFilter
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellListState
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_spells
import kotlin.coroutines.cancellation.CancellationException

class SpellListViewModel(
    private val router: SpellRouter,
    private val repository: SpellRepository,
) : ViewModel() {

    private var updateJob: Job? = null
    val state: StateFlow<SpellListState>
        field = MutableStateFlow(SpellListState(body = SpellListState.Body.Empty))

    init {
        refreshData()
    }

    fun onNavigateUpClicked() {
        router.navigateUp()
    }

    fun onSearchQueryChanged(query: String) {
        updateFilter { it.copy(query = query) }
    }

    fun onSpellClicked(spell: Spell) {
        router.openSpellDetail(spell)
    }

    fun onLevelToggled(level: Int) {
        updateFilter { it.copy(levels = it.levels.toggled(level)) }
    }

    fun onSchoolToggled(school: Spell.School) {
        updateFilter { it.copy(schools = it.schools.toggled(school)) }
    }

    fun onClassToggled(playerClass: PlayerCharacter.Class) {
        updateFilter { it.copy(playerClasses = it.playerClasses.toggled(playerClass)) }
    }

    fun onResetFilters() {
        updateFilter { SpellFilter(query = it.query) }
    }

    private fun updateFilter(transform: (SpellFilter) -> SpellFilter) {
        state.update { it.copy(filter = transform(it.filter)) }
        refreshData()
    }

    private fun refreshData() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch { updateData() }
    }

    private suspend fun updateData() {
        val filter = state.value.filter
        state.update { it.copy(body = SpellListState.Body.Loading) }

        try {
            val spells = repository.getAll(filter)
            val body = if (spells.isEmpty()) {
                SpellListState.Body.Empty
            } else {
                SpellListState.Body.WithData(spells)
            }
            state.update { it.copy(body = body) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            state.update { it.copy(body = SpellListState.Body.Error(errorMessage = Res.string.error_while_loading_spells)) }
        }
    }
}

package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellFilter
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellListState
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_spells

class SpellBookViewModel(
    private val router: SpellRouter,
    private val repository: SpellRepository,
) : ViewModel() {

    private var updateJob: Job? = null
    private val _state: MutableStateFlow<SpellListState> = MutableStateFlow(
        SpellListState(body = SpellListState.Body.Empty),
    )
    val state: StateFlow<SpellListState> = _state.asStateFlow()

    init {
        refreshData()
    }

    fun onNavigateUpClicked() {
        router.navigateUp()
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(filter = it.filter.copy(query = query)) }
        refreshData()
    }

    fun onSpellClicked(spell: Spell) {
        router.openSpellDetail(spell)
    }

    fun onLevelToggled(level: Int) {
        _state.update {
            val updatedLevels = it.filter.levels.toggled(level)
            it.copy(filter = it.filter.copy(levels = updatedLevels))
        }
        refreshData()
    }

    fun onSchoolToggled(school: Spell.School) {
        _state.update {
            val updatedSchools = it.filter.schools.toggled(school)
            it.copy(filter = it.filter.copy(schools = updatedSchools))
        }
        refreshData()
    }

    fun onClassToggled(playerClass: PlayerCharacter.Class) {
        _state.update {
            val updatedClasses = it.filter.playerClasses.toggled(playerClass)
            it.copy(filter = it.filter.copy(playerClasses = updatedClasses))
        }
        refreshData()
    }

    fun onResetFilters() {
        _state.update {
            it.copy(filter = SpellFilter(query = it.filter.query))
        }
        refreshData()
    }

    private fun <T> Set<T>.toggled(item: T): Set<T> = if (item in this) this - item else this + item

    private fun refreshData() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch { updateData() }
    }

    private suspend fun updateData() {
        _state.update { it.copy(body = SpellListState.Body.Loading) }

        try {
            val filter = _state.value.filter
            val spells = repository.getAll(filter)
            if (spells.isEmpty()) {
                _state.update { it.copy(body = SpellListState.Body.Empty) }
            } else {
                _state.update { it.copy(body = SpellListState.Body.WithData(spells)) }
            }
        } catch (e: Exception) {
            _state.update { it.copy(body = SpellListState.Body.Error(errorMessage = Res.string.error_while_loading_spells)) }
        }
    }
}

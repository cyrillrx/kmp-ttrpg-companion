package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.core.domain.toggled
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellFilter
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellListState
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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

    var savedScrollIndex: Int = 0
        private set

    val scrollToTopEvents: SharedFlow<Unit>
        field = MutableSharedFlow(replay = 0)

    fun saveScrollIndex(index: Int) { savedScrollIndex = index }

    init {
        refreshData()
    }

    fun filterByQuery(query: String) {
        updateFilter { it.copy(query = query) }
    }

    fun onSpellClicked(spell: Spell) {
        router.openDetail(spell.id)
    }

    fun onLevelToggled(level: Int) {
        updateFilter { it.copy(levels = it.levels.toggled(level)) }
    }

    fun onSchoolToggled(school: Spell.School) {
        updateFilter { it.copy(schools = it.schools.toggled(school)) }
    }

    fun onClassToggled(characterClass: Character.Class) {
        updateFilter { it.copy(characterClasses = it.characterClasses.toggled(characterClass)) }
    }

    fun onResetFilters() {
        updateFilter { SpellFilter(query = it.query) }
    }

    private fun updateFilter(transform: (SpellFilter) -> SpellFilter) {
        state.update { it.copy(filter = transform(it.filter)) }
        scrollToTopEvents.tryEmit(Unit)
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

package com.cyrillrx.rpg.spell.presentation.viewmodel

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.core.domain.toggled
import com.cyrillrx.rpg.core.presentation.viewmodel.SearchableListViewModel
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellFilter
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.domain.cycled
import com.cyrillrx.rpg.spell.presentation.SpellListState
import kotlinx.coroutines.flow.update
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_spells

class SpellListViewModel(
    private val repository: SpellRepository,
) : SearchableListViewModel<SpellListState, SpellListState.Body>(
        initialState = SpellListState(body = SpellListState.Body.Loading),
    ) {

    init {
        refresh()
    }

    fun filterByQuery(query: String) {
        updateFilter(debounce = true) { it.copy(query = query) }
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

    fun onComponentToggled(component: Spell.ComponentType) {
        updateFilter { it.copy(components = it.components.cycled(component)) }
    }

    fun onResetFilters() {
        updateFilter { SpellFilter(query = it.query) }
    }

    private fun updateFilter(debounce: Boolean = false, transform: (SpellFilter) -> SpellFilter) {
        mutableState.update { it.copy(filter = transform(it.filter)) }
        refresh(debounce = debounce, resetScroll = true)
    }

    override fun SpellListState.body(): SpellListState.Body = body

    override fun SpellListState.withBody(body: SpellListState.Body): SpellListState = copy(body = body)

    override fun loadingBody(): SpellListState.Body = SpellListState.Body.Loading

    override fun errorBody(): SpellListState.Body =
        SpellListState.Body.Error(errorMessage = Res.string.error_while_loading_spells)

    override fun showsContent(body: SpellListState.Body): Boolean =
        body is SpellListState.Body.WithData || body is SpellListState.Body.Empty

    override suspend fun loadContent(): SpellListState.Body {
        val spells = repository.getAll(mutableState.value.filter)
        return if (spells.isEmpty()) {
            SpellListState.Body.Empty
        } else {
            SpellListState.Body.WithData(spells)
        }
    }
}

package com.cyrillrx.rpg.creature.presentation.viewmodel

import com.cyrillrx.rpg.core.domain.toggled
import com.cyrillrx.rpg.core.presentation.viewmodel.SearchableListViewModel
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.MonsterFilter
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.presentation.MonsterListState
import kotlinx.coroutines.flow.update
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_monsters

class MonsterListViewModel(
    private val repository: MonsterRepository,
) : SearchableListViewModel<MonsterListState, MonsterListState.Body>(
        initialState = MonsterListState(body = MonsterListState.Body.Loading),
    ) {

    init {
        refresh()
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
        mutableState.update { it.copy(filter = transform(it.filter)) }
        refresh(debounce = debounce, resetScroll = true)
    }

    override fun MonsterListState.body(): MonsterListState.Body = body

    override fun MonsterListState.withBody(body: MonsterListState.Body): MonsterListState = copy(body = body)

    override fun loadingBody(): MonsterListState.Body = MonsterListState.Body.Loading

    override fun errorBody(): MonsterListState.Body =
        MonsterListState.Body.Error(errorMessage = Res.string.error_while_loading_monsters)

    override fun showsContent(body: MonsterListState.Body): Boolean =
        body is MonsterListState.Body.WithData || body is MonsterListState.Body.Empty

    override suspend fun loadContent(): MonsterListState.Body {
        val monsters = repository.getAll(mutableState.value.filter)
        return if (monsters.isEmpty()) {
            MonsterListState.Body.Empty
        } else {
            MonsterListState.Body.WithData(searchResults = monsters)
        }
    }
}

package com.cyrillrx.rpg.creature.presentation

import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureFilter
import org.jetbrains.compose.resources.StringResource

data class CreatureListState(
    val filter: CreatureFilter = CreatureFilter(),
    val body: Body,
) {
    sealed interface Body {
        data object Loading : Body

        data object Empty : Body

        data class Error(val errorMessage: StringResource) : Body

        data class WithData(val searchResults: List<Creature>) : Body
    }
}

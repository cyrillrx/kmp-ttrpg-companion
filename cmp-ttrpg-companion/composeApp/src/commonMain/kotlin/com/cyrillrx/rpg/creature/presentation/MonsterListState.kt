package com.cyrillrx.rpg.creature.presentation

import com.cyrillrx.rpg.creature.domain.MonsterFilter
import com.cyrillrx.rpg.creature.domain.Monster
import org.jetbrains.compose.resources.StringResource

data class MonsterListState(
    val filter: MonsterFilter = MonsterFilter(),
    val body: Body,
) {
    sealed interface Body {
        data object Loading : Body
        data object Empty : Body
        data class Error(val errorMessage: StringResource) : Body
        data class WithData(val searchResults: List<Monster>) : Body
    }
}

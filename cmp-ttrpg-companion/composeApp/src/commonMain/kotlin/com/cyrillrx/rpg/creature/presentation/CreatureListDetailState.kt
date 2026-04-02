package com.cyrillrx.rpg.creature.presentation

import com.cyrillrx.rpg.creature.domain.Creature
import org.jetbrains.compose.resources.StringResource

data class CreatureListDetailState(
    val listName: String = "",
    val body: Body = Body.Loading,
) {
    sealed interface Body {
        data object Loading : Body
        data object EmptyList : Body
        data class Error(val errorMessage: StringResource) : Body
        data class WithData(val creatures: List<Creature>) : Body
    }
}

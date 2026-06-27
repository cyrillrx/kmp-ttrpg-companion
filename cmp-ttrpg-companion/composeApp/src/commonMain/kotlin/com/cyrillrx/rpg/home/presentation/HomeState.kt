package com.cyrillrx.rpg.home.presentation

import com.cyrillrx.rpg.character.domain.Character
import org.jetbrains.compose.resources.StringResource

data class HomeState(
    val body: Body,
) {
    sealed interface Body {
        data object Loading : Body
        data class Error(val errorMessage: StringResource) : Body
        data class WithData(val characters: List<Character>) : Body
    }
}

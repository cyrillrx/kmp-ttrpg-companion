package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.Character
import org.jetbrains.compose.resources.StringResource

data class CharacterListState(
    val searchQuery: String,
    val body: Body,
) {
    sealed interface Body {
        data object Loading : Body
        data object Empty : Body
        data class Error(val errorMessage: StringResource) : Body
        data class WithData(val searchResults: List<Character>) : Body
    }
}

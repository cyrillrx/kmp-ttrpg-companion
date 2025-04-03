package com.cyrillrx.rpg.character

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import org.jetbrains.compose.resources.StringResource

data class PlayerCharacterListState(
    val searchQuery: String,
    val body: Body,
) {
    sealed interface Body {
        data object Loading : Body

        data object Empty : Body

        data class Error(val errorMessage: StringResource) : Body

        data class WithData(val searchResults: List<PlayerCharacter>) : Body
    }
}

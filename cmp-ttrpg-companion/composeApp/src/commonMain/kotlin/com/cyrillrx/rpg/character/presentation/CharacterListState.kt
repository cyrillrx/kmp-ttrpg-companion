package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.core.domain.StoredSortOrder
import org.jetbrains.compose.resources.StringResource

data class CharacterListState(
    val searchQuery: String,
    val body: Body,
    val sortOrder: StoredSortOrder = StoredSortOrder.LAST_MODIFIED,
) {
    sealed interface Body {
        data object Loading : Body
        data object Empty : Body
        data class Error(val errorMessage: StringResource) : Body
        data class WithData(val searchResults: List<Stored<Character>>) : Body
    }
}

package com.cyrillrx.rpg.bestiary.presentation

import com.cyrillrx.rpg.bestiary.domain.Creature

sealed class BestiaryState(
    open val searchQuery: String = "",
) {
    data class Loading(override val searchQuery: String) : BestiaryState()

    data class Empty(override val searchQuery: String) : BestiaryState()

    data class Error(
        override val searchQuery: String,
        val errorMessage: String,
    ) : BestiaryState()

    data class WithData(
        override val searchQuery: String,
        val searchResults: List<Creature> = emptyList(),
    ) : BestiaryState()
}

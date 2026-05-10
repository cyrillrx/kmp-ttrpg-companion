package com.cyrillrx.rpg.character.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.core.navigation.navigateUp

interface CharacterRouter {
    fun navigateUp()
    fun openCharacterDetail(character: Character)
    fun openCreateCharacter()
}

class CharacterRouterImpl(private val backStack: NavBackStack<NavKey>) : CharacterRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openCreateCharacter() {
        backStack.add(CharacterRoute.Create)
    }

    override fun openCharacterDetail(character: Character) {
        backStack.add(CharacterRoute.Detail(character.serialize()))
    }
}

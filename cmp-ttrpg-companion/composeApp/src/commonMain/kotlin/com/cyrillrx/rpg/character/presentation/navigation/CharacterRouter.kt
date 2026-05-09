package com.cyrillrx.rpg.character.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.core.navigation.navigateUp

interface PlayerCharacterRouter {
    fun navigateUp()
    fun openPlayerCharacterDetail(character: PlayerCharacter)
    fun openCreatePlayerCharacter()
}

class PlayerCharacterRouterImpl(private val backStack: NavBackStack<NavKey>) : PlayerCharacterRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openCreatePlayerCharacter() {
        backStack.add(PlayerCharacterRoute.Create)
    }

    override fun openPlayerCharacterDetail(character: PlayerCharacter) {
        backStack.add(PlayerCharacterRoute.Detail(character.serialize()))
    }
}

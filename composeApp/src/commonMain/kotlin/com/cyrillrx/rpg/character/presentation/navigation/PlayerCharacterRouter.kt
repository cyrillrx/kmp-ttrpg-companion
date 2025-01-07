package com.cyrillrx.rpg.character.presentation.navigation

import androidx.navigation.NavController
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.character.domain.PlayerCharacter

interface PlayerCharacterRouter {
    fun navigateUp()
    fun openPlayerCharacterDetail(character: PlayerCharacter)
    fun openCreatePlayerCharacter()
}

class PlayerCharacterRouterImpl(private val navController: NavController) : PlayerCharacterRouter {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun openCreatePlayerCharacter() {
        navController.navigate(PlayerCharacterRoute.Create)
    }

    override fun openPlayerCharacterDetail(character: PlayerCharacter) {
        navController.navigate(PlayerCharacterRoute.Detail(character.serialize()))
    }
}

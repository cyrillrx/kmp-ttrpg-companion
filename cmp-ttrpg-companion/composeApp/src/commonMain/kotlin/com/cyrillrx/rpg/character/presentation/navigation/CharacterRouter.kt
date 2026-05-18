package com.cyrillrx.rpg.character.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.core.navigation.navigateUp

interface CharacterRouter {
    fun navigateUp()
    fun openCharacterDetail(characterId: String)
    fun openCreateCharacter()
    fun openPresetGallery()
}

class CharacterRouterImpl(private val backStack: NavBackStack<NavKey>) : CharacterRouter {
    override fun navigateUp() {
        backStack.navigateUp()
    }

    override fun openCharacterDetail(characterId: String) {
        backStack.add(CharacterRoute.Detail(characterId))
    }

    override fun openCreateCharacter() {
        backStack.add(CharacterRoute.Create)
    }

    override fun openPresetGallery() {
        backStack.add(CharacterRoute.PresetGallery)
    }
}

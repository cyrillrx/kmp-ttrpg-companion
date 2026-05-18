package com.cyrillrx.rpg.character.presentation.viewmodel

import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter

internal class NoOpCharacterRouter : CharacterRouter {
    override fun navigateUp() = Unit
    override fun openCharacterDetail(characterId: String) = Unit
    override fun openCreateCharacter() = Unit
    override fun openPresetGallery() = Unit
}

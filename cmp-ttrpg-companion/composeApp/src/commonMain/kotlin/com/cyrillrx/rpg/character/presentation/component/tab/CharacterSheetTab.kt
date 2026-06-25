package com.cyrillrx.rpg.character.presentation.component.tab

import org.jetbrains.compose.resources.StringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_aptitudes
import rpg_companion.composeapp.generated.resources.label_combat
import rpg_companion.composeapp.generated.resources.label_profile

internal enum class CharacterSheetTab(val label: StringResource) {
    APTITUDES(Res.string.label_aptitudes),
    COMBAT(Res.string.label_combat),
    PROFILE(Res.string.label_profile),
}

package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.Character
import org.jetbrains.compose.resources.StringResource

data class CharacterPresetGalleryState(
    val selectedTabIndex: Int = 0,
    val body: Body = Body.Loading,
) {
    sealed interface Body {
        data object Loading : Body

        data class Error(
            val errorMessage: StringResource,
        ) : Body

        data class WithData(
            val pcPresets: List<Character>,
            val npcPresets: List<Character>,
        ) : Body
    }
}

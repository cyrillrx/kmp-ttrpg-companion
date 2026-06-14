package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.Character

sealed interface CharacterEditState {
    data object Loading : CharacterEditState
    data class NotFound(val characterId: String) : CharacterEditState

    data class Loaded(
        val character: Character,
        val editingField: EditingField? = null,
    ) : CharacterEditState {

        sealed interface EditingField {
            data object Race : EditingField
            data object Clazz : EditingField
            data object Level : EditingField
            data object Background : EditingField
            data object ShortDescription : EditingField
            data object Strength : EditingField
            data object Dexterity : EditingField
            data object Constitution : EditingField
            data object Intelligence : EditingField
            data object Wisdom : EditingField
            data object Charisma : EditingField
            data object ArmorClass : EditingField
            data object MaxHitPoints : EditingField
            data object WalkSpeed : EditingField
            data object Languages : EditingField
            data object Alignment : EditingField
            data object Skills : EditingField
        }
    }
}

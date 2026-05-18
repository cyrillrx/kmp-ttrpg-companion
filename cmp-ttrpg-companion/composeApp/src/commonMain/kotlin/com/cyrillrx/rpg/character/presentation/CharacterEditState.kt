package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.core.presentation.component.dnd.defaultWalkSpeed
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Creature

sealed interface CharacterEditState {
    data object Loading : CharacterEditState
    data class NotFound(val characterId: String) : CharacterEditState

    data class Body(
        val name: String,
        val race: Race,
        val clazz: Character.Class,
        val level: Int,
        val background: String,
        val strength: Int,
        val dexterity: Int,
        val constitution: Int,
        val intelligence: Int,
        val wisdom: Int,
        val charisma: Int,
        val armorClass: Int,
        val maxHitPoints: Int,
        val walkSpeed: Int,
        val languages: List<Language>,
        val alignment: Creature.Alignment,
        val editingField: EditingField? = null,
    ) : CharacterEditState {
        sealed interface EditingField {
            data object Name : EditingField
            data object Race : EditingField
            data object Clazz : EditingField
            data object Level : EditingField
            data object Background : EditingField
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
        }

        companion object {
            fun from(character: Character): Body = Body(
                name = character.name,
                race = character.race,
                clazz = character.clazz,
                level = character.level,
                background = character.background.orEmpty(),
                strength = character.abilities.str.value,
                dexterity = character.abilities.dex.value,
                constitution = character.abilities.con.value,
                intelligence = character.abilities.int.value,
                wisdom = character.abilities.wis.value,
                charisma = character.abilities.cha.value,
                armorClass = character.armorClass,
                maxHitPoints = character.maxHitPoints,
                walkSpeed = character.speeds.walk ?: character.race.defaultWalkSpeed(),
                languages = character.languages,
                alignment = character.alignment,
            )
        }
    }
}

fun CharacterEditState.Body.toCharacter(original: Character): Character = original.copy(
    name = name,
    race = race,
    clazz = clazz,
    level = level,
    background = background.takeIf { it.isNotBlank() },
    abilities = Abilities(
        str = Ability(strength, original.abilities.str.savingThrowProficiency),
        dex = Ability(dexterity, original.abilities.dex.savingThrowProficiency),
        con = Ability(constitution, original.abilities.con.savingThrowProficiency),
        int = Ability(intelligence, original.abilities.int.savingThrowProficiency),
        wis = Ability(wisdom, original.abilities.wis.savingThrowProficiency),
        cha = Ability(charisma, original.abilities.cha.savingThrowProficiency),
    ),
    armorClass = armorClass,
    maxHitPoints = maxHitPoints,
    speeds = original.speeds.copy(walk = walkSpeed),
    languages = languages,
    alignment = alignment,
)

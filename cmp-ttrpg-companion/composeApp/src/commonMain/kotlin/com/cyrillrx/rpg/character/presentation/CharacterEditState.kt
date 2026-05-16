package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Creature

data class CharacterEditState(
    val name: String,
    val race: Race,
    val clazz: Character.Class,
    val level: Int,
    val background: String,
    val str: Int,
    val dex: Int,
    val con: Int,
    val intelligence: Int,
    val wis: Int,
    val cha: Int,
    val armorClass: Int,
    val maxHitPoints: Int,
    val walkSpeed: Int?,
    val languages: List<Language>,
    val alignment: Creature.Alignment,
    val editingField: EditingField? = null,
) {
    sealed interface EditingField {
        data object Name : EditingField
        data object Race : EditingField
        data object Clazz : EditingField
        data object Level : EditingField
        data object Background : EditingField
        data object Str : EditingField
        data object Dex : EditingField
        data object Con : EditingField
        data object Intelligence : EditingField
        data object Wis : EditingField
        data object Cha : EditingField
        data object ArmorClass : EditingField
        data object MaxHitPoints : EditingField
        data object WalkSpeed : EditingField
        data object Languages : EditingField
        data object Alignment : EditingField
    }

    companion object {
        fun from(character: Character): CharacterEditState = CharacterEditState(
            name = character.name,
            race = character.race,
            clazz = character.clazz,
            level = character.level,
            background = character.background.orEmpty(),
            str = character.abilities.str.value,
            dex = character.abilities.dex.value,
            con = character.abilities.con.value,
            intelligence = character.abilities.int.value,
            wis = character.abilities.wis.value,
            cha = character.abilities.cha.value,
            armorClass = character.armorClass,
            maxHitPoints = character.maxHitPoints,
            walkSpeed = character.speeds.walk,
            languages = character.languages,
            alignment = character.alignment,
        )
    }
}

fun CharacterEditState.toCharacter(original: Character): Character = original.copy(
    name = name,
    race = race,
    clazz = clazz,
    level = level,
    background = background.takeIf { it.isNotBlank() },
    abilities = Abilities(
        str = Ability(str, original.abilities.str.savingThrowProficiency),
        dex = Ability(dex, original.abilities.dex.savingThrowProficiency),
        con = Ability(con, original.abilities.con.savingThrowProficiency),
        int = Ability(intelligence, original.abilities.int.savingThrowProficiency),
        wis = Ability(wis, original.abilities.wis.savingThrowProficiency),
        cha = Ability(cha, original.abilities.cha.savingThrowProficiency),
    ),
    armorClass = armorClass,
    maxHitPoints = maxHitPoints,
    speeds = original.speeds.copy(walk = walkSpeed),
    languages = languages,
    alignment = alignment,
)

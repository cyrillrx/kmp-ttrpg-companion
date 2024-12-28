package com.cyrillrx.rpg.creature.domain

open class Abilities(
    strValue: Int,
    dexValue: Int,
    conValue: Int,
    intValue: Int,
    wisValue: Int,
    chaValue: Int,
) {
    val str = Ability(strValue)
    val dex = Ability(dexValue)
    val con = Ability(conValue)
    val int = Ability(intValue)
    val wis = Ability(wisValue)
    val cha = Ability(chaValue)
}

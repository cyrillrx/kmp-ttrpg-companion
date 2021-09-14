package com.cyrillrx.rpg.models.bestiary

/**
 * @author Cyril Leroux
 *         Created on 13/03/2020.
 */
class Creature(
    name: String,
    description: String,
    type: String,
    subtype: String,
    size: String,
    alignment: String,
    abilities: Abilities
) : Character(name, description, type, subtype, size, alignment, abilities) {
}
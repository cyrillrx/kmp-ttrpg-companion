package com.cyrillrx.rpg.bestiary.domain

class Creature(
    name: String,
    description: String,
    type: String,
    subtype: String,
    size: String,
    alignment: String,
    abilities: Abilities,
) : Character(name, description, type, subtype, size, alignment, abilities)

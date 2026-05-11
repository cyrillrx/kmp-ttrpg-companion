package com.cyrillrx.rpg.creature.data

import com.cyrillrx.rpg.creature.domain.Creature

internal fun String.toSize(): Creature.Size? =
    Creature.Size.entries.find { it.name.equals(this, ignoreCase = true) }

internal fun String.toAlignment(): Creature.Alignment? =
    Creature.Alignment.entries.find { it.name.equals(this, ignoreCase = true) }

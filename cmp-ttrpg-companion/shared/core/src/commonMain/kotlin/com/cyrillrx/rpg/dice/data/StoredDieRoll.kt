package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DieRoll
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import kotlinx.serialization.Serializable

@Serializable
internal data class StoredDieRoll(
    val die: Die,
    val value: Int,
    val discarded: Int? = null,
    val rule: KeepRule? = null,
) {
    /** Throws [IllegalArgumentException] when the stored values no longer make a valid roll. */
    fun toDieRoll(): DieRoll {
        val kept = SimpleRoll(die, value)
        if (discarded == null && rule == null) return kept

        return DoubleRoll(
            kept = kept,
            discarded = SimpleRoll(die, requireNotNull(discarded)),
            rule = requireNotNull(rule),
        )
    }
}

internal fun DieRoll.toStored(): StoredDieRoll = when (this) {
    is SimpleRoll -> StoredDieRoll(die, value)
    is DoubleRoll -> StoredDieRoll(die, value, discarded = discarded.value, rule = rule)
}

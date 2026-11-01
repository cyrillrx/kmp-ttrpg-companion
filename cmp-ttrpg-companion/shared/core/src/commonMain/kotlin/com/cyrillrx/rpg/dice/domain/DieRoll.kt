package com.cyrillrx.rpg.dice.domain

/** A die and the value that counts toward a total. */
sealed interface DieRoll {
    val die: Die
    val value: Int
}

data class SimpleRoll(override val die: Die, override val value: Int) : DieRoll {
    init {
        require(value in 1..die.sides) { "$die cannot roll $value" }
    }
}

/** Two rolls of the same die, of which only [kept] counts, chosen by [rule]. */
data class DoubleRoll(val kept: SimpleRoll, val discarded: SimpleRoll, val rule: KeepRule) : DieRoll by kept {
    init {
        require(kept.die == discarded.die) { "Cannot keep a ${kept.die} over a ${discarded.die}" }
        require(rule.keeps(kept.value, over = discarded.value)) {
            "Keeping the $rule roll cannot keep ${kept.value} over ${discarded.value}"
        }
    }
}

internal fun DieRoll.toTerm(): DiceTerm = when (this) {
    is SimpleRoll -> DiceTerm(count = 1, die = die)
    is DoubleRoll -> DiceTerm(count = DOUBLE_ROLL_DICE, die = die, keep = rule)
}

/** Which of two rolls counts; with equal rolls, either does. */
enum class KeepRule(internal val notation: String) {
    HIGHER(notation = "kh1"),
    LOWER(notation = "kl1"),
    ;

    internal fun keeps(value: Int, over: Int): Boolean = when (this) {
        HIGHER -> value >= over
        LOWER -> value <= over
    }
}

package com.cyrillrx.rpg.dice.data

import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.dice.domain.DiceExpression
import com.cyrillrx.rpg.dice.domain.DiceTerm
import com.cyrillrx.rpg.dice.domain.DieRoll
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import com.cyrillrx.rpg.dice.domain.keepOne
import com.cyrillrx.rpg.dice.domain.termOrder
import com.cyrillrx.rpg.dice.domain.toTerm

private const val TERM_SEPARATOR = "+"
private const val VALUE_SEPARATOR = ","
private val VALUES = Regex("""\([^)]*\)""")
private val RECORDED_TERM = Regex("""([^+()]+)\(([^)]*)\)""")

/** The pool as its notation, each term followed by the values it rolled, such as `2d20kh1(15,10)+1d6(5)+2`. */
internal fun RollPool.toRecord(): String = buildString {
    append(
        recordedTerms().joinToString(TERM_SEPARATOR) { (term, values) ->
            "$term(${values.joinToString(VALUE_SEPARATOR)})"
        },
    )
    if (modifier > 0) append(TERM_SEPARATOR)
    if (modifier != 0) append(modifier)
}

/** Throws [IllegalArgumentException] when the record no longer reads as a valid pool. */
internal fun String.toRollPool(): RollPool {
    val expression = parseNotation(replace(VALUES, ""))
    val rolls = RECORDED_TERM.findAll(this).flatMap { match ->
        val (termNotation, valuesText) = match.destructured
        parseNotation(termNotation).terms.single().toRolls(valuesText.split(VALUE_SEPARATOR).map(String::toInt))
    }
    val pool = RollPool(rolls.toList(), expression.modifier)
    require(pool.toExpression() == expression) { "$this records values for only some of its dice" }

    return pool
}

private fun RollPool.recordedTerms(): List<Pair<DiceTerm, List<Int>>> {
    val doubleRolls = rolls.filterIsInstance<DoubleRoll>().map {
        it.toTerm() to
            listOf(it.kept.value, it.discarded.value)
    }
    val simpleRolls = rolls.filterIsInstance<SimpleRoll>().groupBy(SimpleRoll::die).map { (die, sameDie) ->
        DiceTerm(sameDie.size, die) to sameDie.map(SimpleRoll::value)
    }
    return (doubleRolls + simpleRolls).sortedWith(compareBy(termOrder) { it.first })
}

private fun parseNotation(notation: String): DiceExpression = when (val result = DiceExpression.parse(notation)) {
    is Result.Success -> result.value
    is Result.Failure -> throw IllegalArgumentException("Cannot read $notation: ${result.error}")
}

private fun DiceTerm.toRolls(values: List<Int>): List<DieRoll> {
    require(values.size == count) { "$this cannot have rolled $values" }

    val rolls = values.map { SimpleRoll(die, it) }
    return when (keep) {
        null -> rolls
        else -> listOf(keep.keepOne(rolls[0], rolls[1]))
    }
}

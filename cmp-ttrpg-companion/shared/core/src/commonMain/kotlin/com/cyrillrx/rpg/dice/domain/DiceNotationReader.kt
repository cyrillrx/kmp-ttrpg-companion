package com.cyrillrx.rpg.dice.domain

import com.cyrillrx.core.domain.Result

internal fun readDiceNotation(notation: String): Result<DiceExpression, DiceNotationError> {
    if (notation.isBlank()) return Result.Failure(DiceNotationError.Blank)

    val parts = mutableListOf<NotationPart>()
    for (text in notation.split(SIGN_BOUNDARY).filterNot(String::isBlank)) {
        when (val part = text.trim().toNotationPart()) {
            is Result.Success -> parts += part.value
            is Result.Failure -> return part
        }
    }
    return parts.toDiceExpression()
}

private sealed interface NotationPart {
    data class Dice(val term: DiceTerm) : NotationPart
    data class Constant(val value: Int) : NotationPart
}

private val SIGN_BOUNDARY = Regex("(?=[+-])")
private val DICE = Regex("""(\d*)[dD](\d+)""")
private val CONSTANT = Regex("""\d+""")

private fun String.toNotationPart(): Result<NotationPart, DiceNotationError> {
    val sign = take(1).takeIf { it == "+" || it == "-" }.orEmpty()
    val body = drop(sign.length).trim()
    val part = sign + body
    val isSubtracted = sign == "-"
    val dice = DICE.matchEntire(body)
    return when {
        body.isEmpty() -> Result.Failure(DiceNotationError.DanglingSign)
        dice != null && isSubtracted -> Result.Failure(DiceNotationError.SubtractedDice(part))
        dice != null -> dice.toDicePart(part)
        CONSTANT.matches(body) -> body.toConstantPart(isSubtracted)
        else -> Result.Failure(DiceNotationError.Unreadable(part))
    }
}

private fun MatchResult.toDicePart(part: String): Result<NotationPart, DiceNotationError> {
    val (countText, sidesText) = destructured
    val count = countText.ifEmpty { "1" }.toIntOrNull()
    val die = Die.entries.find { it.sides.toString() == sidesText }
    return when {
        count == 0 -> Result.Failure(DiceNotationError.ZeroDice(part))
        count == null || count > MAX_DICE_COUNT -> Result.Failure(DiceNotationError.TooManyDice)
        die == null -> Result.Failure(DiceNotationError.UnsupportedDie("d$sidesText"))
        else -> Result.Success(NotationPart.Dice(DiceTerm(count, die)))
    }
}

private fun String.toConstantPart(isSubtracted: Boolean): Result<NotationPart, DiceNotationError> {
    val value = toIntOrNull() ?: return Result.Failure(DiceNotationError.ModifierOutOfRange)

    return Result.Success(NotationPart.Constant(if (isSubtracted) -value else value))
}

private fun List<NotationPart>.toDiceExpression(): Result<DiceExpression, DiceNotationError> {
    val terms = filterIsInstance<NotationPart.Dice>().map { it.term }
    val modifier = filterIsInstance<NotationPart.Constant>().sumOf { it.value.toLong() }
    return when {
        terms.isEmpty() -> Result.Failure(DiceNotationError.NoDice)
        terms.sumOf { it.count } > MAX_DICE_COUNT -> Result.Failure(DiceNotationError.TooManyDice)
        modifier !in -MAX_MODIFIER..MAX_MODIFIER -> Result.Failure(DiceNotationError.ModifierOutOfRange)
        else -> Result.Success(DiceExpression(terms, modifier.toInt()))
    }
}

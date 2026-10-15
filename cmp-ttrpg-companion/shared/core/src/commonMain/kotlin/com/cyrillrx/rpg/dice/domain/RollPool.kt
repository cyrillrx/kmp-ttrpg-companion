package com.cyrillrx.rpg.dice.domain

data class RollPool(val rolls: List<DieRoll> = emptyList(), val modifier: Int = 0) {
    val total: Int get() = rolls.sumOf { it.kept } + modifier

    val isEmpty: Boolean get() = rolls.isEmpty()

    fun add(roll: DieRoll): RollPool = copy(rolls = rolls + roll)

    fun remove(index: Int): RollPool {
        require(index in rolls.indices) { "No roll at index $index in a pool of ${rolls.size}" }

        return copy(rolls = rolls.filterIndexed { i, _ -> i != index })
    }

    fun withModifier(modifier: Int): RollPool = copy(modifier = modifier)
}

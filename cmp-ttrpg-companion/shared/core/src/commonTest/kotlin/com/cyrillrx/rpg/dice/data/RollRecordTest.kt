package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RollRecordTest {

    private val advantage = DoubleRoll(
        kept = SimpleRoll(Die.D20, value = 15),
        discarded = SimpleRoll(Die.D20, value = 10),
        rule = KeepRule.HIGHER,
    )
    private val pool = RollPool(
        listOf(SimpleRoll(Die.D4, value = 3), SimpleRoll(Die.D6, value = 5), advantage, SimpleRoll(Die.D6, value = 1)),
        modifier = 2,
    )

    @Test
    fun `a pool is recorded as its notation with the values of each term`() {
        assertEquals("2d20kh1(15,10)+2d6(5,1)+1d4(3)+2", pool.toRecord())
    }

    @Test
    fun `a negative modifier is recorded as a subtraction`() {
        assertEquals("1d4(3)-2", RollPool(listOf(SimpleRoll(Die.D4, value = 3)), modifier = -2).toRecord())
    }

    @Test
    fun `a pool without dice is not recorded`() {
        assertFailsWith<IllegalArgumentException> { RollPool(modifier = 2).toRecord() }
    }

    @Test
    fun `a record reads back as the pool it was written from`() {
        assertEquals(pool.toRecord(), pool.toRecord().toRollPool().toRecord())
    }

    @Test
    fun `a double roll reads back keeping the value its rule keeps whatever the order`() {
        assertEquals(RollPool(listOf(advantage)), "2d20kh1(10,15)".toRollPool())
    }

    @Test
    fun `values read back on their own term whatever the term order`() {
        val expected = RollPool(listOf(SimpleRoll(Die.D4, value = 3), SimpleRoll(Die.D20, value = 12)))

        assertEquals(expected.toRecord(), "1d4(3)+1d20(12)".toRollPool().toRecord())
    }

    @Test
    fun `a record that no longer reads as a valid pool is refused`() {
        listOf("1d7(3)", "1d4(9)", "2d6(4)", "1d6(4,2)", "1d6", "1d6(x)", "2d20kh2(3,4)").forEach { record ->
            assertFailsWith<IllegalArgumentException>(record) { record.toRollPool() }
        }
    }
}

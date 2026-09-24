package com.cyrillrx.rpg.creature.data

import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.data.shippedDataFile
import com.cyrillrx.rpg.creature.data.api.ApiMonster
import com.cyrillrx.rpg.creature.domain.isValidArmorClass
import com.cyrillrx.rpg.creature.domain.isValidCreatureSpeedInFeet
import com.cyrillrx.rpg.creature.domain.isValidMaxHitPoints
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * The import clamps silently, so a bound set too tight would rewrite the bestiary without anyone
 * noticing. This reads the file the app actually ships and asserts nothing in it needs coercing.
 */
class ShippedMonsterBoundsTest {

    @Test
    fun `no shipped monster is out of bounds`() {
        val monsters = shippedDataFile("monsters.json").readText().deserialize<List<ApiMonster>>()
        assertTrue(monsters.size > 500, "expected the full bestiary, found ${monsters.size}")

        val offenders = monsters.flatMap { monster ->
            val id = monster.id ?: "<no id>"
            buildList {
                monster.armorClass
                    ?.takeUnless { isValidArmorClass(it) }
                    ?.let { add("$id armor class $it") }
                monster.maxHitPoints
                    ?.takeUnless { isValidMaxHitPoints(it) }
                    ?.let { add("$id max hit points $it") }
                monster.speeds?.let { speeds ->
                    listOf(
                        "walk" to speeds.walk,
                        "fly" to speeds.fly,
                        "swim" to speeds.swim,
                        "climb" to speeds.climb,
                        "burrow" to speeds.burrow,
                    ).forEach { (mode, value) ->
                        value?.takeUnless { isValidCreatureSpeedInFeet(it) }?.let { add("$id $mode $it") }
                    }
                }
            }
        }

        assertTrue(offenders.isEmpty(), "shipped monsters outside the creature bounds: $offenders")
    }
}

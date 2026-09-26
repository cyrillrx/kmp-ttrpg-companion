package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.data.shippedDataFile
import com.cyrillrx.rpg.character.data.api.ApiCharacter
import com.cyrillrx.rpg.character.domain.isValidCharacterLevel
import com.cyrillrx.rpg.character.domain.isValidWalkSpeedInFeet
import com.cyrillrx.rpg.creature.domain.isValidArmorClass
import com.cyrillrx.rpg.creature.domain.isValidCreatureSpeedInFeet
import com.cyrillrx.rpg.creature.domain.isValidMaxHitPoints
import kotlin.test.Test
import kotlin.test.assertTrue

/** The import clamps silently, so a bound set too tight would rewrite the bundled presets unnoticed. */
class ShippedPresetBoundsTest {

    @Test
    fun `no shipped preset is out of bounds`() {
        val offenders = PRESET_FILES.flatMap { fileName ->
            val presets = shippedDataFile(fileName).readText().deserialize<List<ApiCharacter>>()
            assertTrue(presets.isNotEmpty(), "expected presets in $fileName")
            presets.flatMap { it.offendingFields(fileName) }
        }

        assertTrue(offenders.isEmpty(), "shipped presets outside the character bounds: $offenders")
    }

    private fun ApiCharacter.offendingFields(fileName: String): List<String> {
        val label = "$fileName/${id ?: "<no id>"}"
        return buildList {
            armorClass
                ?.takeUnless { isValidArmorClass(it) }
                ?.let { add("$label armor class $it") }
            maxHitPoints
                ?.takeUnless { isValidMaxHitPoints(it) }
                ?.let { add("$label max hit points $it") }
            classes?.forEach { (name, level) ->
                if (!isValidCharacterLevel(level)) add("$label $name level $level")
            }
            speeds?.let { speeds ->
                speeds.walk
                    ?.takeUnless { isValidWalkSpeedInFeet(it) }
                    ?.let { add("$label walk $it") }
                listOf(
                    "fly" to speeds.fly,
                    "swim" to speeds.swim,
                    "climb" to speeds.climb,
                    "burrow" to speeds.burrow,
                ).forEach { (mode, value) ->
                    value?.takeUnless { isValidCreatureSpeedInFeet(it) }?.let { add("$label $mode $it") }
                }
            }
        }
    }

    private companion object {
        val PRESET_FILES = listOf("pc-presets.json", "npc-presets.json")
    }
}

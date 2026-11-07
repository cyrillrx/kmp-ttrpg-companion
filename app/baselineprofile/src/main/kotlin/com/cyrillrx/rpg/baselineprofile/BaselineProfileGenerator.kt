package com.cyrillrx.rpg.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    /** Startup profile: the cold-launch path to the home screen only. */
    @Test
    fun generateStartupProfile() = rule.collect(
        packageName = APP_PACKAGE,
        includeInStartupProfile = true,
    ) {
        pressHome()
        startActivityAndWait()
    }

    /** Baseline profile: open and scroll each compendium list. */
    @Test
    fun generate() = rule.collect(packageName = APP_PACKAGE) {
        pressHome()
        startActivityAndWait()

        COMPENDIUM_ENTRY_TAGS.forEach { entryTag ->
            openAndScroll(entryTag)
            device.pressBack()
            device.wait(Until.hasObject(By.res(HOME_ANCHOR_TAG)), WAIT_TIMEOUT)
        }
    }
}

private fun MacrobenchmarkScope.openAndScroll(entryTag: String) {
    val entry = device.wait(Until.findObject(By.res(entryTag)), WAIT_TIMEOUT)
        ?: error("Home entry '$entryTag' not found; the baseline profile would silently miss it.")
    entry.click()
    device.waitForIdle()

    // Re-find the list each iteration: a fling can re-layout it and invalidate a previously captured
    // UiObject2 (StaleObjectException). The stable testTag (exposed as a resource id) targets the
    // LazyColumn container reliably, unlike By.scrollable(true).
    repeat(SCROLL_ITERATIONS) {
        val list = device.wait(Until.findObject(By.res(COMPENDIUM_LIST_TAG)), WAIT_TIMEOUT)
            ?: error("Compendium list '$COMPENDIUM_LIST_TAG' not found after opening '$entryTag'.")
        list.setGestureMargin(device.displayWidth / 5)
        list.fling(Direction.DOWN)
        device.waitForIdle()
    }
}

private const val APP_PACKAGE = "com.cyrillrx.rpg"
private const val WAIT_TIMEOUT = 5_000L
private const val SCROLL_ITERATIONS = 3

// This module cannot depend on the ui module, so the tags are duplicated rather than referenced.
// Targeting by resource id (not localized home-button text) keeps generation independent of the
// device locale.
private const val COMPENDIUM_LIST_TAG = "compendium_list"
private const val HOME_SPELL_ENTRY_TAG = "home_spell_entry"
private const val HOME_MAGICAL_ITEM_ENTRY_TAG = "home_magical_item_entry"
private const val HOME_MONSTER_ENTRY_TAG = "home_monster_entry"
private val COMPENDIUM_ENTRY_TAGS = listOf(HOME_SPELL_ENTRY_TAG, HOME_MAGICAL_ITEM_ENTRY_TAG, HOME_MONSTER_ENTRY_TAG)

// The spell entry doubles as the home anchor waited on after returning from each list.
private const val HOME_ANCHOR_TAG = HOME_SPELL_ENTRY_TAG

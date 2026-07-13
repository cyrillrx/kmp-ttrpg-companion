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

/**
 * Generates a baseline profile exercising the three compendium lists (open + scroll).
 *
 * Run on the managed device with `./gradlew :androidApp:generateBaselineProfile`. Labels are the
 * default (English) home-button texts, matching the GMD's default locale.
 */
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() = rule.collect(packageName = APP_PACKAGE) {
        pressHome()
        startActivityAndWait()

        COMPENDIUM_ENTRIES.forEach { label ->
            openAndScroll(label)
            device.pressBack()
            device.wait(Until.hasObject(By.text(HOME_ANCHOR)), WAIT_TIMEOUT)
        }
    }
}

private fun MacrobenchmarkScope.openAndScroll(entryLabel: String) {
    val entry = device.wait(Until.findObject(By.text(entryLabel)), WAIT_TIMEOUT) ?: return
    entry.click()
    device.waitForIdle()

    // Re-find the list each iteration: a fling can re-layout it and invalidate a previously captured
    // UiObject2 (StaleObjectException). The stable testTag (exposed as a resource id) targets the
    // LazyColumn container reliably, unlike By.scrollable(true).
    repeat(SCROLL_ITERATIONS) {
        val list = device.wait(Until.findObject(By.res(COMPENDIUM_LIST_TAG)), WAIT_TIMEOUT)
            ?: return@repeat
        list.setGestureMargin(device.displayWidth / 5)
        list.fling(Direction.DOWN)
        device.waitForIdle()
    }
}

private const val APP_PACKAGE = "com.cyrillrx.rpg"
private const val WAIT_TIMEOUT = 5_000L
private const val SCROLL_ITERATIONS = 3
private const val HOME_ANCHOR = "Spellbook"
private const val COMPENDIUM_LIST_TAG = "compendium_list"
private val COMPENDIUM_ENTRIES = listOf("Spellbook", "Bestiary", "Magical items")

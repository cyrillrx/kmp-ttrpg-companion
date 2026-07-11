package com.cyrillrx.rpg.baselineprofile

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Measures scroll jank on the spell list with and without the baseline profile.
 *
 * Compare `frameDurationCpuMs` (P50/P90/P99) between [scrollNoCompilation] (interpreted, i.e. the
 * warmup case) and [scrollBaselineProfile] (AOT-compiled hot paths). Run with
 * `./gradlew :baselineprofile:pixel6Api34BenchmarkAndroidTest`.
 */
@RunWith(AndroidJUnit4::class)
class ScrollBenchmark {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun scrollNoCompilation() = scroll(CompilationMode.None())

    @Test
    fun scrollBaselineProfile() = scroll(CompilationMode.Partial())

    private fun scroll(mode: CompilationMode) = rule.measureRepeated(
        packageName = APP_PACKAGE,
        metrics = listOf(FrameTimingMetric()),
        compilationMode = mode,
        startupMode = StartupMode.COLD,
        iterations = ITERATIONS,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            device.wait(Until.findObject(By.text(SPELL_LIST_ENTRY)), WAIT_TIMEOUT)?.click()
            device.waitForIdle()
        },
    ) {
        val list = device.wait(Until.findObject(By.scrollable(true)), WAIT_TIMEOUT)
            ?: return@measureRepeated
        list.setGestureMargin(device.displayWidth / 5)
        repeat(SCROLL_ITERATIONS) {
            list.fling(Direction.DOWN)
            device.waitForIdle()
        }
    }
}

private const val APP_PACKAGE = "com.cyrillrx.rpg"
private const val WAIT_TIMEOUT = 5_000L
private const val ITERATIONS = 10
private const val SCROLL_ITERATIONS = 3
private const val SPELL_LIST_ENTRY = "Spellbook"

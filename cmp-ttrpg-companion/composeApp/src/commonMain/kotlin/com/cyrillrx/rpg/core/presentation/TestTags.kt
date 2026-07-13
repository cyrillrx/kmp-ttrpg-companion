package com.cyrillrx.rpg.core.presentation

/**
 * Stable UI test tags. Exposed to UiAutomator as resource ids via `testTagsAsResourceId` (see the
 * Android entry point), so macrobenchmarks can target scrollable containers reliably instead of the
 * transient nodes matched by `By.scrollable(true)`.
 */
const val COMPENDIUM_LIST_TEST_TAG = "compendium_list"

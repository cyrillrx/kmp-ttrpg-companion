package com.cyrillrx.rpg.core.presentation

/**
 * Stable UI test tags. Exposed to UiAutomator as resource ids via `testTagsAsResourceId` (see the
 * Android entry point), so macrobenchmarks can target scrollable containers reliably instead of the
 * transient nodes matched by `By.scrollable(true)`.
 *
 * The `:baselineprofile` module cannot depend on this module, so it duplicates this value as
 * `COMPENDIUM_LIST_TAG`; keep the two in sync.
 */
const val COMPENDIUM_LIST_TEST_TAG = "compendium_list"

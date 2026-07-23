package com.cyrillrx.rpg.core.presentation

/**
 * Stable UI test tags. Exposed to UiAutomator as resource ids via `testTagsAsResourceId` (see the
 * Android entry point), so macrobenchmarks can target nodes reliably regardless of device locale
 * (unlike matching localized button text) or transient layout (unlike `By.scrollable(true)`).
 *
 * The `:baselineprofile` module cannot depend on this module, so it duplicates these values; keep
 * the two in sync.
 */
const val COMPENDIUM_LIST_TEST_TAG = "compendium_list"

const val HOME_SPELL_ENTRY_TEST_TAG = "home_spell_entry"
const val HOME_MAGICAL_ITEM_ENTRY_TEST_TAG = "home_magical_item_entry"
const val HOME_MONSTER_ENTRY_TEST_TAG = "home_monster_entry"

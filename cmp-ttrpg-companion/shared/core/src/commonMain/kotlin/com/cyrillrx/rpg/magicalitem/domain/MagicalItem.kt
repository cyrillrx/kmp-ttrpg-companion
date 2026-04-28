package com.cyrillrx.rpg.magicalitem.domain

import kotlinx.serialization.Serializable

private const val FALLBACK_LOCALE = "en"

@Serializable
class MagicalItem(
    val id: String,
    val source: String,
    val type: Type,
    val rarity: Rarity,
    val attunement: Boolean,
    val translations: Map<String, Translation>,
) {
    init {
        require(translations.isNotEmpty()) { "MagicalItem $id must have at least one translation" }
    }

    fun resolveTranslation(locale: String): Translation =
        translations[locale]
            ?: translations[FALLBACK_LOCALE]
            ?: requireNotNull(translations.entries.minByOrNull { it.key }?.value) {
                "MagicalItem $id has empty translations"
            }

    enum class Type { ARMOR, POTION, RING, ROD, SCROLL, STAFF, WAND, WEAPON, WONDROUS_ITEM }
    enum class Rarity { COMMON, UNCOMMON, RARE, VERY_RARE, LEGENDARY, ARTIFACT }

    @Serializable
    data class Translation(
        val name: String,
        val subtype: String?,
        val description: String,
    )
}

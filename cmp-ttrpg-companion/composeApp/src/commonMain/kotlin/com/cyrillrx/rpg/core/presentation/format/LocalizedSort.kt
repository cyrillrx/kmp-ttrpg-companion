package com.cyrillrx.rpg.core.presentation.format

/**
 * Latin letters stripped of their diacritics. Kotlin common has no `Normalizer`, so the mapping is explicit.
 * Ligatures (æ, œ, ß) expand to several letters and are deliberately left alone.
 */
private val DIACRITIC_FOLDING: Map<Char, Char> = buildMap {
    fold("àáâãäåāăą", 'a')
    fold("çćĉċč", 'c')
    fold("ďđ", 'd')
    fold("èéêëēĕėęě", 'e')
    fold("ĝğġģ", 'g')
    fold("ìíîïĩīĭįı", 'i')
    fold("ĺļľł", 'l')
    fold("ñńņň", 'n')
    fold("òóôõöøōŏő", 'o')
    fold("ŕŗř", 'r')
    fold("śŝşš", 's')
    fold("ţťŧ", 't')
    fold("ùúûüũūŭůűų", 'u')
    fold("ýÿŷ", 'y')
    fold("źżž", 'z')
}

private fun MutableMap<Char, Char>.fold(accented: String, base: Char) {
    accented.forEach { put(it, base) }
}

/** Maps lowercase letters only; [localizedSortKey] lowercases beforehand. */
internal fun String.foldDiacritics(): String = map { DIACRITIC_FOLDING[it] ?: it }.joinToString("")

/** Sort key approximating alphabetical order in the current language, accents included. */
fun String.localizedSortKey(): String = lowercase().foldDiacritics()

fun <T> List<T>.sortedByLocalizedName(nameOf: (T) -> String): List<T> =
    sortedBy { nameOf(it).localizedSortKey() }

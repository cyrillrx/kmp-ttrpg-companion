package com.cyrillrx.core.domain

/**
 * Latin letters stripped of their diacritics, in both cases. Kotlin common has no `Normalizer`, so the
 * mapping is explicit. Ligatures map to the letters they stand for, so they sort where a reader expects
 * them rather than after `z`, where their code point would put them.
 */
private val DIACRITIC_FOLDING: Map<Char, String> = buildMap {
    fold("àáâãäåāăą", "a")
    fold("çćĉċč", "c")
    fold("ďđ", "d")
    fold("èéêëēĕėęě", "e")
    fold("ĝğġģ", "g")
    fold("ìíîïĩīĭįı", "i")
    fold("ĺļľł", "l")
    fold("ñńņň", "n")
    fold("òóôõöøōŏő", "o")
    fold("ŕŗř", "r")
    fold("śŝşš", "s")
    fold("ţťŧ", "t")
    fold("ùúûüũūŭůűų", "u")
    fold("ýÿŷ", "y")
    fold("źżž", "z")
    fold("æ", "ae")
    fold("œ", "oe")
    fold("ß", "ss")
}

private fun MutableMap<Char, String>.fold(accented: String, base: String) {
    accented.forEach {
        put(it, base)
        // ß has no single-character uppercase, so uppercaseChar() hands back ß itself: mapping it
        // here would overwrite the lowercase entry with SS.
        val upperCase = it.uppercaseChar()
        if (upperCase != it) put(upperCase, base.uppercase())
    }
}

internal fun String.foldDiacritics(): String = map { DIACRITIC_FOLDING[it] ?: it.toString() }.joinToString("")

/** Sort key approximating alphabetical order in the current language, accents included. */
fun String.localizedSortKey(): String = lowercase().foldDiacritics()

fun <T> List<T>.sortedByLocalizedName(nameOf: (T) -> String): List<T> =
    sortedBy { nameOf(it).localizedSortKey() }

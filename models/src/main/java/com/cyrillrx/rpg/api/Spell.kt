package com.cyrillrx.rpg.api

import com.sun.xml.internal.fastinfoset.util.StringArray

/**
 * @author Cyril Leroux
 *         Created on 10/03/2020.
 */
class Spell(
    val title: String,
    val content: String,
    val level: Int,
    val casting_time: String,
    val range: String,
    val components: String,
    val duration: String,
    val header: Header
) {
    fun getSpellClasses() = header.taxonomy.spell_class

    class Header(val taxonomy: Taxonomy) {

        class Taxonomy(val spell_class: StringArray)
    }
}
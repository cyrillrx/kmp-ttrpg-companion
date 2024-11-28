package com.cyrillrx.rpg.api.spellbook

import kotlinx.serialization.Serializable

@Serializable
class ApiSpell(
    val title: String,
    val content: String,
    val level: Int,
    val casting_time: String,
    val range: String,
    val components: String,
    val duration: String,
    val header: Header,
) {
    fun getSpellClasses(): Array<String> = header.taxonomy.spell_class

    fun getSchool(): String = header.taxonomy.spell_school.joinToString(", ")

    @Serializable
    class Header(val taxonomy: Taxonomy) {
        @Serializable
        class Taxonomy(
            val spell_school: Array<String>,
            val spell_level: Array<String>,
            val spell_class: Array<String>,
        )
    }
}

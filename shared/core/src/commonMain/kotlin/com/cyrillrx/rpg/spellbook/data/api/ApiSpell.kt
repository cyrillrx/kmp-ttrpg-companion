package com.cyrillrx.rpg.spellbook.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiSpell(
    val title: String?,
    val content: String?,
    val level: Int?,
    val casting_time: String?,
    val range: String?,
    val components: String?,
    val duration: String?,
    val header: Header?,
) {
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

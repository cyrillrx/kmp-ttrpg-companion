package com.cyrillrx.rpg.server.campaign.domain

enum class RuleSet {
    DND5E,
    PATHFINDER_2E,
    STARFINDER,
    CALL_OF_CTHULHU_7E,
    VAMPIRE_THE_MASQUERADE_5E,
    OTHER,
    UNDEFINED,
    ;

    companion object {
        fun fromInt(value: Int): RuleSet {
            return when (value) {
                0 -> DND5E
                1 -> PATHFINDER_2E
                2 -> STARFINDER
                3 -> CALL_OF_CTHULHU_7E
                4 -> VAMPIRE_THE_MASQUERADE_5E
                5 -> OTHER
                else -> UNDEFINED
            }
        }
    }
}
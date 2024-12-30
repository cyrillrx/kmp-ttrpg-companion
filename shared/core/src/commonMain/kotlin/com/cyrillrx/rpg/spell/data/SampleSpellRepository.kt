package com.cyrillrx.rpg.spell.data

import com.cyrillrx.rpg.spell.domain.Spell

class SampleSpellRepository {
    fun getAll(): List<Spell> {
        val sample = get()
        return listOf(sample, sample, sample)
    }

    fun get(): Spell = Spell(
        title = "Fire",
        description = "Une traînée luisante part de votre doigt tendu et file vers un point de votre choix situé à portée et dans votre champ de vision, où elle explose dans une gerbe de flammes grondantes. Chaque créature située dans une sphère de 6 mètres de rayon centrée sur ce point doit faire un jet de sauvegarde de Dextérité. Celles qui échouent subissent 8d6 dégâts de feu, les autres la moitié seulement.\n" +
            "\n" +
            "Le feu s'étend en contournant les angles. Il embrase les objets inflammables de la zone, à moins que quelqu'un ne les porte ou ne les transporte.\n" +
            "\n" +
            "À plus haut niveau. Si vous lancez ce sort en utilisant un emplacement de niveau 4 ou supérieur, les dégâts augmentent de 1d6 par niveau au-delà du niveau 3.",
        level = 3,
        castingTime = "1 action",
        range = "45 mètres",
        components = "V, S, M (une petite boule de guano de chauve-souris et du soufre)",
        duration = "instantanée",
        schools = listOf(Spell.School.EVOCATION),
        availableClasses = listOf(com.cyrillrx.rpg.character.domain.Character.Class.SORCERER, com.cyrillrx.rpg.character.domain.Character.Class.WIZARD),
    )
}

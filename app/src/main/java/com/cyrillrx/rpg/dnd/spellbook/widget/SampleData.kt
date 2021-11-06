package com.cyrillrx.rpg.dnd.spellbook.widget

import com.cyrillrx.rpg.api.spellbook.Spell

internal fun sampleSpell(): Spell {
    val schools = arrayOf("Evocation")
    val levels = arrayOf("3")
    val classes = arrayOf("Ensorceleur/Sorcelame", "Magicien")
    val header = Spell.Header(Spell.Header.Taxonomy(schools, levels, classes))
    return Spell(
        "Fire",
        "Une traînée luisante part de votre doigt tendu et file vers un point de votre choix situé à portée et dans votre champ de vision, où elle explose dans une gerbe de flammes grondantes. Chaque créature située dans une sphère de 6 mètres de rayon centrée sur ce point doit faire un jet de sauvegarde de Dextérité. Celles qui échouent subissent 8d6 dégâts de feu, les autres la moitié seulement.\n" +
                "\n" +
                "Le feu s'étend en contournant les angles. Il embrase les objets inflammables de la zone, à moins que quelqu'un ne les porte ou ne les transporte.\n" +
                "\n" +
                "À plus haut niveau. Si vous lancez ce sort en utilisant un emplacement de niveau 4 ou supérieur, les dégâts augmentent de 1d6 par niveau au-delà du niveau 3.",
        3,
        "1 action",
        "45 mètres",
        "V, S, M (une petite boule de guano de chauve-souris et du soufre)",
        "instantanée",
        header
    )
}

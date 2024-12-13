package com.cyrillrx.rpg.magicalitems.data

import com.cyrillrx.rpg.magicalitems.domain.MagicalItem

class SampleMagicalItemsRepository {
    fun getAll(): List<MagicalItem> {
        val sample = get()
        return listOf(sample, sample, sample)
    }

    fun get(): MagicalItem {
        val description =
            "Lorsque vous faites une attaque au corps à corps avec cette arme, vous pouvez prononcer son mot de commande : « Prompte mort à ceux qui m'ont fait du tort ». La cible de votre attaque devient votre ennemi juré jusqu'à ce qu'il meure ou jusqu'à l'aube sept jours plus tard.\n" +
                "Vous ne pouvez avoir qu'un seul ennemi juré à la fois. Quand votre ennemi juré meurt, vous pouvez en choisir un nouveau après la prochaine aube.\n" +
                "Lorsque vous effectuez une attaque au corps à corps contre votre ennemi juré avec cette arme, vous avez un avantage au jet d'attaque.\n" +
                "Si l'attaque touche, votre ennemi juré subit 3d6 dégâts tranchants supplémentaires.\n" +
                "Tant que votre ennemi juré est en vie, vous avez un désavantage aux jets d'attaque avec toutes les autres armes."
        return MagicalItem(
            title = "Hache du serment",
            subtitle = "Arme (hache d'arme), unique (harmonisation exigée)",
            description = description,
            type = MagicalItem.Type.WEAPON,
            rarity = MagicalItem.Rarity.RARE,
            attunement = true,
        )
    }
}

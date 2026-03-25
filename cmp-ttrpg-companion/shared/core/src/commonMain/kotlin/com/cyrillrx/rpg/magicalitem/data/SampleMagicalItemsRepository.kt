package com.cyrillrx.rpg.magicalitem.data

import com.cyrillrx.rpg.magicalitem.domain.MagicalItem

class SampleMagicalItemsRepository {
    fun getAll(): List<MagicalItem> = listOf(
        MagicalItem(
            id = "Hache du serment",
            title = "Hache du serment",
            subtitle = "Arme (hache d'arme), unique (harmonisation exigée)",
            description = "Lorsque vous faites une attaque au corps à corps avec cette arme, vous pouvez prononcer son mot de commande. La cible de votre attaque devient votre ennemi juré.",
            type = MagicalItem.Type.WEAPON,
            rarity = MagicalItem.Rarity.RARE,
            attunement = true,
        ),
        MagicalItem(
            id = "Cape de protection",
            title = "Cape de protection",
            subtitle = "Objet merveilleux, peu commun (harmonisation exigée)",
            description = "Vous obtenez un bonus de +1 à la CA et aux jets de sauvegarde tant que vous portez cette cape.",
            type = MagicalItem.Type.WONDROUS_ITEM,
            rarity = MagicalItem.Rarity.UNCOMMON,
            attunement = true,
        ),
        MagicalItem(
            id = "Potion de soins",
            title = "Potion de soins",
            subtitle = "Potion, commune",
            description = "Vous regagnez 2d4 + 2 points de vie lorsque vous buvez cette potion.",
            type = MagicalItem.Type.POTION,
            rarity = MagicalItem.Rarity.COMMON,
            attunement = false,
        ),
        MagicalItem(
            id = "Bouclier +1",
            title = "Bouclier +1",
            subtitle = "Armure (bouclier), peu commun",
            description = "Lorsque vous tenez ce bouclier, vous obtenez un bonus de +1 à la CA en plus du bonus normal du bouclier.",
            type = MagicalItem.Type.ARMOR,
            rarity = MagicalItem.Rarity.UNCOMMON,
            attunement = false,
        ),
        MagicalItem(
            id = "Baguette de boules de feu",
            title = "Baguette de boules de feu",
            subtitle = "Baguette, rare (harmonisation exigée par un lanceur de sorts)",
            description = "Cette baguette a 7 charges. En tenant la baguette, vous pouvez utiliser une action pour dépenser 1 ou plusieurs charges et lancer le sort boule de feu.",
            type = MagicalItem.Type.WAND,
            rarity = MagicalItem.Rarity.RARE,
            attunement = true,
        ),
        MagicalItem(
            id = "Anneau de protection",
            title = "Anneau de protection",
            subtitle = "Anneau, rare (harmonisation exigée)",
            description = "Vous obtenez un bonus de +1 à la CA et aux jets de sauvegarde tant que vous portez cet anneau.",
            type = MagicalItem.Type.RING,
            rarity = MagicalItem.Rarity.RARE,
            attunement = true,
        ),
        MagicalItem(
            id = "Parchemin de boule de feu",
            title = "Parchemin de boule de feu",
            subtitle = "Parchemin, peu commun",
            description = "Un sort de boule de feu est inscrit sur ce parchemin. Si le sort figure dans votre liste de sorts, vous pouvez le lancer.",
            type = MagicalItem.Type.SCROLL,
            rarity = MagicalItem.Rarity.UNCOMMON,
            attunement = false,
        ),
        MagicalItem(
            id = "Bâton de pouvoir",
            title = "Bâton de pouvoir",
            subtitle = "Bâton, très rare (harmonisation exigée par un lanceur de sorts)",
            description = "Ce bâton confère un bonus de +2 aux jets d'attaque et de dégâts des attaques au corps à corps effectuées avec lui.",
            type = MagicalItem.Type.STAFF,
            rarity = MagicalItem.Rarity.VERY_RARE,
            attunement = true,
        ),
    )

    fun get(): MagicalItem = getAll().first()

    fun getById(id: String): MagicalItem? = getAll().firstOrNull { it.id == id }
}

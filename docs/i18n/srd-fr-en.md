# D&D 5e SRD — Translation Map

Canonical mapping of D&D 5e SRD mechanical terms across languages.

This document serves as a reference for:
- **Data ingestion**: transforming source data into normalized JSON (see [`data-ingestion.md`](data-ingestion.md))
- **Auto-translation assistance**: populating `translations.en` from `translations.fr` entries, or vice versa
- **UI localization**: resolving display strings from JSON enum values in the app

All JSON enum values use the **English key** column in lowercase with underscores (e.g., `"conjuration"`, `"humanoid"`).

**SRD versions covered**: `srd_5.1` (2014) and `srd_5.2` (2024). Sections marked with a version apply to that version only; unmarked sections apply to both.

---

## Ability Scores

> Applies to: `srd_5.1`, `srd_5.2`

| English key | Display EN   | Display FR    |
|-------------|--------------|---------------|
| `str`       | Strength     | Force         |
| `dex`       | Dexterity    | Dextérité     |
| `con`       | Constitution | Constitution  |
| `int`       | Intelligence | Intelligence  |
| `wis`       | Wisdom       | Sagesse       |
| `cha`       | Charisma     | Charisme      |

---

## Proficiency Levels

> Applies to: `srd_5.1`, `srd_5.2`

| English key  | Display EN | Display FR  |
|--------------|------------|-------------|
| `none`       | —          | —           |
| `proficient` | Proficient | Maîtrise    |
| `expert`     | Expertise  | Expertise   |

---

## Spell Schools

> Applies to: `srd_5.1`, `srd_5.2`

| English key     | Display EN    | Display FR    |
|-----------------|---------------|---------------|
| `abjuration`    | Abjuration    | Abjuration    |
| `conjuration`   | Conjuration   | Invocation    |
| `divination`    | Divination    | Divination    |
| `enchantment`   | Enchantment   | Enchantement  |
| `evocation`     | Evocation     | Évocation     |
| `illusion`      | Illusion      | Illusion      |
| `necromancy`    | Necromancy    | Nécromancie   |
| `transmutation` | Transmutation | Transmutation |

---

## Character Classes

> Applies to: `srd_5.1`, `srd_5.2`

| English key  | Display EN | Display FR            |
|--------------|------------|-----------------------|
| `barbarian`  | Barbarian  | Barbare               |
| `bard`       | Bard       | Barde                 |
| `cleric`     | Cleric     | Clerc                 |
| `druid`      | Druid      | Druide                |
| `fighter`    | Fighter    | Guerrier              |
| `monk`       | Monk       | Moine                 |
| `paladin`    | Paladin    | Paladin               |
| `ranger`     | Ranger     | Rôdeur                |
| `rogue`      | Rogue      | Roublard              |
| `sorcerer`   | Sorcerer   | Ensorceleur/Sorcelame |
| `warlock`    | Warlock    | Occultiste            |
| `wizard`     | Wizard     | Magicien              |

---

## Creature Types

> Applies to: `srd_5.1`, `srd_5.2`

| English key   | Display EN  | Display FR              |
|---------------|-------------|-------------------------|
| `aberration`  | Aberration  | Aberration              |
| `beast`       | Beast       | Bête                    |
| `celestial`   | Celestial   | Céleste                 |
| `construct`   | Construct   | Créature artificielle   |
| `dragon`      | Dragon      | Dragon                  |
| `elemental`   | Elemental   | Élémentaire             |
| `fey`         | Fey         | Fée                     |
| `fiend`       | Fiend       | Fiélon                  |
| `giant`       | Giant       | Géant                   |
| `humanoid`    | Humanoid    | Humanoïde               |
| `monstrosity` | Monstrosity | Monstruosité            |
| `ooze`        | Ooze        | Vase                    |
| `plant`       | Plant       | Plante                  |
| `undead`      | Undead      | Mort-vivant             |

---

## Creature Sizes

> Applies to: `srd_5.1`, `srd_5.2`

| English key  | Display EN | Display FR   | FR code |
|--------------|------------|--------------|---------|
| `tiny`       | Tiny       | Très petite  | TP      |
| `small`      | Small      | Petite       | P       |
| `medium`     | Medium     | Moyenne      | M       |
| `large`      | Large      | Grande       | G       |
| `huge`       | Huge       | Très grande  | TG      |
| `gargantuan` | Gargantuan | Gigantesque  | Gig     |

---

## Alignments

> Applies to: `srd_5.1`, `srd_5.2`

| English key        | Display EN        | Display FR            |
|--------------------|-------------------|-----------------------|
| `lawful_good`      | Lawful Good       | Loyal bon             |
| `lawful_neutral`   | Lawful Neutral    | Loyal neutre          |
| `lawful_evil`      | Lawful Evil       | Loyal mauvais         |
| `neutral_good`     | Neutral Good      | Neutre bon            |
| `neutral`          | Neutral           | Neutre                |
| `neutral_evil`     | Neutral Evil      | Neutre mauvais        |
| `chaotic_good`     | Chaotic Good      | Chaotique bon         |
| `chaotic_neutral`  | Chaotic Neutral   | Chaotique neutre      |
| `chaotic_evil`     | Chaotic Evil      | Chaotique mauvais     |
| `unaligned`        | Unaligned         | Sans alignement       |

---

## Magical Item Types

> Applies to: `srd_5.1`, `srd_5.2`

| English key     | Display EN    | Display FR        |
|-----------------|---------------|-------------------|
| `armor`         | Armor         | Armure            |
| `potion`        | Potion        | Potion            |
| `ring`          | Ring          | Anneau            |
| `rod`           | Rod           | Sceptre           |
| `scroll`        | Scroll        | Parchemin         |
| `staff`         | Staff         | Bâton             |
| `wand`          | Wand          | Baguette          |
| `weapon`        | Weapon        | Arme              |
| `wondrous_item` | Wondrous Item | Objet merveilleux |

---

## Magical Item Rarities

> Applies to: `srd_5.1`, `srd_5.2`

| English key  | Display EN | Display FR    |
|--------------|------------|---------------|
| `common`     | Common     | Courant       |
| `uncommon`   | Uncommon   | Peu courant   |
| `rare`       | Rare       | Rare          |
| `very_rare`  | Very Rare  | Très rare     |
| `legendary`  | Legendary  | Légendaire    |
| `artifact`   | Artifact   | Artéfact      |

---

## Skills

> Applies to: `srd_5.1`, `srd_5.2`

| English key      | Display EN      | Display FR      | Ability |
|------------------|-----------------|-----------------|---------|
| `athletics`      | Athletics       | Athlétisme      | STR     |
| `acrobatics`     | Acrobatics      | Acrobaties      | DEX     |
| `sleightOfHand`  | Sleight of Hand | Escamotage      | DEX     |
| `stealth`        | Stealth         | Discrétion      | DEX     |
| `arcana`         | Arcana          | Arcanes         | INT     |
| `history`        | History         | Histoire        | INT     |
| `investigation`  | Investigation   | Investigation   | INT     |
| `nature`         | Nature          | Nature          | INT     |
| `religion`       | Religion        | Religion        | INT     |
| `animalHandling` | Animal Handling | Dressage        | WIS     |
| `insight`        | Insight         | Intuition       | WIS     |
| `medicine`       | Medicine        | Médecine        | WIS     |
| `perception`     | Perception      | Perception      | WIS     |
| `survival`       | Survival        | Survie          | WIS     |
| `deception`      | Deception       | Tromperie       | CHA     |
| `intimidation`   | Intimidation    | Intimidation    | CHA     |
| `performance`    | Performance     | Représentation  | CHA     |
| `persuasion`     | Persuasion      | Persuasion      | CHA     |

---

## Damage Types

> Applies to: `srd_5.1`, `srd_5.2`

| English key               | Display EN                 | Display FR                  |
|---------------------------|----------------------------|-----------------------------|
| `acid`                    | Acid                       | Acide                       |
| `bludgeoning`             | Bludgeoning                | Contondant                  |
| `cold`                    | Cold                       | Froid                       |
| `fire`                    | Fire                       | Feu                         |
| `force`                   | Force                      | Force                       |
| `lightning`               | Lightning                  | Foudre                      |
| `necrotic`                | Necrotic                   | Nécrotique                  |
| `piercing`                | Piercing                   | Perforant                   |
| `poison`                  | Poison                     | Poison                      |
| `psychic`                 | Psychic                    | Psychique                   |
| `radiant`                 | Radiant                    | Radiant                     |
| `slashing`                | Slashing                   | Tranchant                   |
| `thunder`                 | Thunder                    | Tonnerre                    |
| `bludgeoningNonMagical`   | Bludgeoning (non-magical)  | Contondant (non magique)    |
| `piercingNonMagical`      | Piercing (non-magical)     | Perforant (non magique)     |
| `slashingNonMagical`      | Slashing (non-magical)     | Tranchant (non magique)     |

---

## Damage Affinities

> Applies to: `srd_5.1`, `srd_5.2`

| English key  | Display EN | Display FR    |
|--------------|------------|---------------|
| `none`       | —          | —             |
| `resistant`  | Resistant  | Résistance    |
| `immune`     | Immune     | Immunité      |
| `vulnerable` | Vulnerable | Vulnérabilité |

---

## Conditions

> Applies to: `srd_5.1`, `srd_5.2`

| English key      | Display EN    | Display FR          |
|------------------|---------------|---------------------|
| `blinded`        | Blinded       | Aveuglé             |
| `charmed`        | Charmed       | Charmé              |
| `deafened`       | Deafened      | Assourdi            |
| `exhaustion`     | Exhaustion    | Épuisement          |
| `frightened`     | Frightened    | Effrayé             |
| `grappled`       | Grappled      | Agrippé             |
| `incapacitated`  | Incapacitated | Incapable d'agir    |
| `paralyzed`      | Paralyzed     | Paralysé            |
| `petrified`      | Petrified     | Pétrifié            |
| `poisoned`       | Poisoned      | Empoisonné          |
| `prone`          | Prone         | À terre             |
| `restrained`     | Restrained    | Entravé             |
| `stunned`        | Stunned       | Étourdi             |
| `unconscious`    | Unconscious   | Inconscient         |

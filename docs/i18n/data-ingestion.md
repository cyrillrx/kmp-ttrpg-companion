# Data Ingestion Guide

Guide for contributors adding or migrating compendium data into the normalized JSON format.

For the complete FR↔EN term mapping, see [`srd-fr-en.md`](srd-fr-en.md).  
For the data model decisions behind these formats, see [`docs/adr/ADR-001-data-model.md`](../adr/ADR-001-data-model.md).

---

## General Rules

- All enum values use **lowercase English with underscores** (e.g., `"conjuration"`, `"wondrous_item"`).
- All keys are in English. French content belongs only inside `translations.fr`.
- The `source` field identifies the origin of the entry — see [known source values](../adr/ADR-001-data-model.md#2-source-field).
- Ability scores are **integers only** — never strings like `"15 (+2)"`.
- Armor class is an **integer only** — the armor type belongs in the creature description.
- No pre-computed modifiers — store scores and proficiency levels only.
- An entity with an empty `translations` map is considered malformed.

---

## Spell Entry Format

```json
{
  "id": "acid-splash",
  "source": "srd_5.1",
  "level": 0,
  "school": "conjuration",
  "concentration": false,
  "ritual": false,
  "components": {
    "verbal": true,
    "somatic": true,
    "material": false
  },
  "availableClasses": ["sorcerer", "wizard"],
  "translations": {
    "fr": {
      "name": "Aspersion acide",
      "castingTime": "1 action",
      "range": "18 mètres",
      "duration": "instantanée",
      "materialDescription": null,
      "description": "<p>...</p>"
    }
  }
}
```

### Spell Checklist
- [ ] `id` — slugified English name (e.g., `"acid-splash"`, `"fireball"`)
- [ ] `source` — source string from known values
- [ ] `level` — integer, `0` for cantrips
- [ ] `school` — see [Spell Schools](srd-fr-en.md#spell-schools)
- [ ] `concentration` — boolean; `true` if the spell requires concentration
- [ ] `ritual` — boolean; `true` if the spell can be cast as a ritual
- [ ] `components.verbal`, `components.somatic`, `components.material` — booleans
- [ ] `availableClasses` — array; see [Character Classes](srd-fr-en.md#character-classes)
- [ ] `translations.fr.name`
- [ ] `translations.fr.castingTime`
- [ ] `translations.fr.range`
- [ ] `translations.fr.duration`
- [ ] `translations.fr.materialDescription` — string or `null`
- [ ] `translations.fr.description` — HTML

---

## Magical Item Entry Format

```json
{
  "id": "amulet-of-health",
  "source": "srd_5.1",
  "type": "wondrous_item",
  "rarity": "uncommon",
  "attunement": true,
  "translations": {
    "fr": {
      "name": "Amulette de cicatrisation",
      "description": "<p>...</p>"
    }
  }
}
```

### Notes
- `attunement`: any non-null French source string (e.g., `"harmonisation requise"`) maps to `true`
- The display subtitle (`"Objet merveilleux · Peu courant · Harmonisation"`) is computed by the client from typed fields — not stored

### Item Checklist
- [ ] `id` — slugified English name
- [ ] `source` — source string from known values
- [ ] `type` — see [Magical Item Types](srd-fr-en.md#magical-item-types)
- [ ] `rarity` — see [Magical Item Rarities](srd-fr-en.md#magical-item-rarities)
- [ ] `attunement` — boolean
- [ ] `translations.fr.name`
- [ ] `translations.fr.description` — HTML

---

## Creature Entry Format

```json
{
  "id": "goblin",
  "source": "srd_5.1",
  "type": "humanoid",
  "subtype": "goblinoid",
  "size": "small",
  "alignment": "neutral_evil",
  "challengeRating": 0.25,
  "armorClass": 15,
  "maxHitPoints": 7,
  "hitDice": "2d6",
  "speed": { "walk": 9 },
  "abilities": {
    "str": { "value": 8,  "savingThrowProficiency": "none" },
    "dex": { "value": 14, "savingThrowProficiency": "none" },
    "con": { "value": 10, "savingThrowProficiency": "none" },
    "int": { "value": 10, "savingThrowProficiency": "none" },
    "wis": { "value": 8,  "savingThrowProficiency": "none" },
    "cha": { "value": 8,  "savingThrowProficiency": "none" }
  },
  "skills": {
    "acrobatics": "none", "animalHandling": "none", "arcana": "none",
    "athletics": "none", "deception": "none", "history": "none",
    "insight": "none", "intimidation": "none", "investigation": "none",
    "medicine": "none", "nature": "none", "perception": "none",
    "performance": "none", "persuasion": "none", "religion": "none",
    "sleightOfHand": "none", "stealth": "proficient", "survival": "none"
  },
  "senses": { "darkvision": 18 },
  "damageAffinities": {},
  "conditionImmunities": {},
  "translations": {
    "fr": {
      "name": "Gobelin",
      "languages": ["commun", "gobelin"],
      "description": "<h2>Actions</h2><p>...</p>"
    }
  }
}
```

### Notes
- `abilities`: raw integer scores only; `savingThrowProficiency` is `"proficient"` or `"expert"` when applicable
- `skills`: all 18 keys required; only non-`none` values need to be set
- `senses`: only include present senses (distances in meters); omit null senses entirely
- `damageAffinities`: only include non-`none` values; omit the rest
- `conditionImmunities`: only include `true` values; omit the rest
- `hitDice`: format is `"NdX"` or `"NdX+Y"` (e.g., `"2d6"`, `"6d8+6"`)
- `languages` is locale-specific — stored in `translations`
- `description` is locale-specific HTML containing traits, actions, reactions, legendary actions

### Creature Checklist
- [ ] `id` — slugified English or transliterated name
- [ ] `source` — source string from known values
- [ ] `type` — see [Creature Types](srd-fr-en.md#creature-types)
- [ ] `subtype` — lowercase English string or omit if none
- [ ] `size` — see [Creature Sizes](srd-fr-en.md#creature-sizes)
- [ ] `alignment` — see [Alignments](srd-fr-en.md#alignments)
- [ ] `challengeRating` — float (e.g., `0.25`, `0.5`, `1.0`, `20.0`)
- [ ] `armorClass` — integer only (no armor type string)
- [ ] `maxHitPoints` — integer (average)
- [ ] `hitDice` — string (e.g., `"2d6"`, `"6d8+6"`)
- [ ] `speed.walk` — integer in meters; add `swim`, `fly`, `climb`, `burrow` if applicable
- [ ] `abilities.*` — each with integer `value` and `savingThrowProficiency`
- [ ] `skills` — all 18 keys present
- [ ] `senses` — only present senses, in meters
- [ ] `damageAffinities` — see [Damage Affinities](srd-fr-en.md#damage-affinities)
- [ ] `conditionImmunities` — see [Conditions](srd-fr-en.md#conditions)
- [ ] `isCustom` — boolean
- [ ] `translations.fr.name`
- [ ] `translations.fr.languages` — array of strings
- [ ] `translations.fr.description` — HTML

---

## Validation Rules

- **No unknown fallback without documentation**: if a source value has no matching entry in the translation map, document it in [`srd-fr-en.md`](srd-fr-en.md) before adding it to the data file.
- **Ability scores must be integers**: reject `"15 (+2)"` — store `15` only.
- **AC must be an integer**: reject `"11 (armure de cuir)"` — store `11` only.
- **Languages belong in `translations`**: never store language names as locale-independent data.
- **No pre-computed modifiers**: store scores and proficiency levels; let the app compute bonuses.

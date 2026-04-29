# SRD Monster Ingestion Guide

Guide for contributors adding or migrating monster data into the normalized JSON format.

For the complete FR↔EN term mapping, see [`docs/i18n/srd-fr-en.md`](../docs/i18n/srd-fr-en.md).  
For the data model decisions behind these formats, see [`docs/adr/ADR-001-data-model.md`](../docs/adr/ADR-001-data-model.md).

---

## General Rules

- All enum values use **lowercase English with underscores** (e.g., `"humanoid"`, `"neutral_evil"`).
- All keys are in English. French content belongs only inside `translations.fr`.
- The `source` field identifies the origin of the entry — see [known source values](../docs/adr/ADR-001-data-model.md#2-source-field).
- An entity with an empty `translations` map is considered malformed.

---

## Monster Entry Format

```json
{
  "id": "goblin",
  "source": "monster_manual_2024",
  "type": "humanoid",
  "size": "small",
  "alignment": "neutral_evil",
  "challengeRating": 0.25,
  "armorClass": 15,
  "maxHitPoints": 7,
  "hitDice": "2d6",
  "abilities": {
    "str": { "value": 8,  "savingThrowProficiency": "none" },
    "dex": { "value": 14, "savingThrowProficiency": "none" },
    "con": { "value": 10, "savingThrowProficiency": "none" },
    "int": { "value": 10, "savingThrowProficiency": "none" },
    "wis": { "value": 8,  "savingThrowProficiency": "none" },
    "cha": { "value": 8,  "savingThrowProficiency": "none" }
  },
  "speeds": {
    "walk": 30,
    "fly": null,
    "swim": null,
    "climb": null,
    "burrow": null,
    "hover": false
  },
  "skills": {
    "acrobatics": "none", "animalHandling": "none", "arcana": "none",
    "athletics": "none", "deception": "none", "history": "none",
    "insight": "none", "intimidation": "none", "investigation": "none",
    "medicine": "none", "nature": "none", "perception": "none",
    "performance": "none", "persuasion": "none", "religion": "none",
    "sleightOfHand": "none", "stealth": "proficient", "survival": "none"
  },
  "damageAffinities": {
    "acid": "none", "bludgeoning": "none", "cold": "none",
    "fire": "none", "force": "none", "lightning": "none",
    "necrotic": "none", "piercing": "none", "poison": "none",
    "psychic": "none", "radiant": "none", "slashing": "none",
    "thunder": "none", "bludgeoningNonMagical": "none",
    "piercingNonMagical": "none", "slashingNonMagical": "none"
  },
  "conditionImmunities": {
    "blinded": false, "charmed": false, "deafened": false,
    "exhaustion": false, "frightened": false, "grappled": false,
    "incapacitated": false, "paralyzed": false, "petrified": false,
    "poisoned": false, "prone": false, "restrained": false,
    "stunned": false, "unconscious": false
  },
  "translations": {
    "en": {
      "name": "Goblin",
      "subtype": "Goblinoid",
      "description": "<h2>Actions</h2><p>...</p>",
      "senses": "Darkvision 60 ft., Passive Perception 9",
      "languages": ["Common", "Goblin"]
    },
    "fr": {
      "name": "Gobelin",
      "subtype": "Gobelinoïde",
      "description": "<h2>Actions</h2><p>...</p>",
      "senses": "Vision dans le noir 18 m, Perception passive 9",
      "languages": ["commun", "gobelin"]
    }
  }
}
```

### Notes

- `abilities.{key}.savingThrowProficiency` — `"none"`, `"proficient"`, or `"expert"` (ADR-001 §5)
- `skills` — all 18 keys required; set to `"none"` when not proficient (ADR-001 §6)
- `damageAffinities` — all 16 keys required; `"none"` / `"resistant"` / `"immune"` / `"vulnerable"` (ADR-001 §7)
- `conditionImmunities` — all 14 keys required; `false` when not immune (ADR-001 §7)
- `hitDice` — format `"NdX"` or `"NdX+Y"` (e.g., `"2d6"`, `"23d8+92"`) (ADR-001 §9)
- `speeds` — all speed values in **feet** (integers); `null` when the movement type does not apply; `hover: true` when the creature hovers; the app converts to metres for FR display (× 0.3, i.e. `feet * 3 / 5` half-metres)
- `translations.{locale}.subtype` — locale-specific subtype string (e.g. EN `"Goblinoid"`, FR `"Gobelinoïde"`), or `null` if the monster has no subtype
- `translations.{locale}.senses` — raw locale text; EN uses feet, FR uses rounded metres
- `translations.{locale}.languages` — array of strings (locale-specific names)
- `translations.{locale}.description` — HTML containing traits, actions, reactions, legendary actions

### Monster Checklist

- [ ] `id` — slugified English name (e.g., `"goblin"`, `"young-red-dragon"`)
- [ ] `source` — source string from known values
- [ ] `type` — see [Creature Types](../docs/i18n/srd-fr-en.md#creature-types)
- [ ] `size` — see [Creature Sizes](../docs/i18n/srd-fr-en.md#creature-sizes)
- [ ] `alignment` — see [Alignments](../docs/i18n/srd-fr-en.md#alignments)
- [ ] `challengeRating` — float (e.g., `0.25`, `0.5`, `1.0`, `20.0`)
- [ ] `armorClass` — integer only (no armor type string)
- [ ] `maxHitPoints` — integer (average)
- [ ] `hitDice` — string (e.g., `"2d6"`, `"23d8+92"`)
- [ ] `abilities` — all 6 keys, each with integer `value` and `savingThrowProficiency`
- [ ] `speeds` — all 5 movement keys (`walk`, `fly`, `swim`, `climb`, `burrow`) + `hover`; values in feet or `null`
- [ ] `skills` — all 18 keys present
- [ ] `damageAffinities` — all 16 keys present
- [ ] `conditionImmunities` — all 14 keys present
- [ ] `translations.{locale}.name`
- [ ] `translations.{locale}.subtype` — locale-specific string or `null`
- [ ] `translations.{locale}.description` — HTML
- [ ] `translations.{locale}.senses` — raw text
- [ ] `translations.{locale}.languages` — array of strings

---

## Damage Affinity Values

| Key            | Meaning             |
|----------------|---------------------|
| `"none"`       | No special affinity |
| `"resistant"`  | Takes half damage   |
| `"immune"`     | Takes no damage     |
| `"vulnerable"` | Takes double damage |

The 16 damage type keys: `acid`, `bludgeoning`, `cold`, `fire`, `force`, `lightning`,
`necrotic`, `piercing`, `poison`, `psychic`, `radiant`, `slashing`, `thunder`,
`bludgeoningNonMagical`, `piercingNonMagical`, `slashingNonMagical`.

---

## Validation Rules

- **No unknown fallback without documentation**: if a source value has no matching entry in the translation map, document it in [`srd-fr-en.md`](../docs/i18n/srd-fr-en.md) before adding it to the data file.
- **Ability scores must be integers**: reject `"15 (+2)"` — store `15` only.
- **AC must be an integer**: reject `"11 (armure de cuir)"` — store `11` only.
- **Speed and senses belong in `translations`**: never store them as locale-independent integers.
- **Languages belong in `translations`**: never store language names as locale-independent data.
- **No pre-computed modifiers**: store scores and proficiency levels; let the app compute bonuses.
- **All bounded objects must be exhaustive**: `skills` (18 keys), `damageAffinities` (16 keys), `conditionImmunities` (14 keys) — all keys always present.

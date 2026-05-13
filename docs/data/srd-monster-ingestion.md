# SRD Monster Ingestion Guide

Guide for contributors adding or migrating monster data into the normalized YAML source format.

For the complete FR↔EN term mapping, see [`docs/data/srd-fr-en.md`](srd-fr-en.md).  
For the data model decisions behind these formats, see [`docs/adr/adr-001-data-model.md`](../adr/adr-001-data-model.md).  
For the source format decision, see [`docs/adr/adr-002-compendium-source-format.md`](../adr/adr-002-compendium-source-format.md).

> **Source files are in `data/compendium/monsters/*.yaml`.**  
> The JSON files (`data/compendium/monsters.json`, `composeResources/files/monsters.json`) are **generated** — run `scripts/build_compendium.py` after editing. Do not edit the JSON files directly.

---

## General Rules

- All enum values use **lowercase English with underscores** (e.g., `"humanoid"`, `"neutral_evil"`).
- All keys are in English. French content belongs only inside `translations.fr`.
- The `source` field identifies the origin of the entry — see [known source values](../adr/adr-001-data-model.md#2-source-field).
- An entity with an empty `translations` map is considered malformed.
- Descriptions use **GitHub Flavored Markdown (GFM)**.

---

## Monster Entry Format

```yaml
id: goblin
source: monster_manual_2024
type: humanoid
size: small
alignment: neutral_evil
challengeRating: 0.25
armorClass: 15
maxHitPoints: 7
hitDice: 2d6
abilities:
  str: 8
  dex: 14
  con: 10
  int: 10
  wis: 8
  cha: 8
savingThrows:
  dex: proficient
speeds:
  walk: 30
  fly: null
  swim: null
  climb: null
  burrow: null
  hover: false
skills:
  acrobatics: none
  animalHandling: none
  arcana: none
  athletics: none
  deception: none
  history: none
  insight: none
  intimidation: none
  investigation: none
  medicine: none
  nature: none
  perception: none
  performance: none
  persuasion: none
  religion: none
  sleightOfHand: none
  stealth: proficient
  survival: none
damageAffinities:
  acid: none
  bludgeoning: none
  cold: none
  fire: none
  force: none
  lightning: none
  necrotic: none
  piercing: none
  poison: none
  psychic: none
  radiant: none
  slashing: none
  thunder: none
  bludgeoningNonMagical: none
  piercingNonMagical: none
  slashingNonMagical: none
conditionImmunities:
  blinded: false
  charmed: false
  deafened: false
  exhaustion: false
  frightened: false
  grappled: false
  incapacitated: false
  paralyzed: false
  petrified: false
  poisoned: false
  prone: false
  restrained: false
  stunned: false
  unconscious: false
translations:
  en:
    name: Goblin
    subtype: Goblinoid
    description: |-
      ## Actions

      **Scimitar.** *Melee Weapon Attack:* +4 to hit, reach 5 ft., one target.
      *Hit:* 5 (1d6 + 2) slashing damage.
    senses: Darkvision 60 ft., Passive Perception 9
    languages:
      - Common
      - Goblin
  fr:
    name: Gobelin
    subtype: Gobelinoïde
    description: |-
      ## Actions

      **Cimeterre.** *Attaque d'arme au corps à corps :* +4 pour toucher, allonge 1,50 m, une cible.
      *Touché :* 5 (1d6 + 2) dégâts tranchants.
    senses: Vision dans le noir 18 m, Perception passive 9

    languages:
      - commun
      - gobelin
```

### Notes

- `abilities.{key}` — integer ability score; all 6 keys required (ADR-001 §5)
- `savingThrows` — map of ability keys to `proficient` or `expert`; omit keys with no proficiency; omit the field entirely when none apply
- `skills` — all 18 keys required; set to `none` when not proficient (ADR-001 §6)
- `damageAffinities` — all 16 keys required; `none` / `resistant` / `immune` / `vulnerable` (ADR-001 §7)
- `conditionImmunities` — all 14 keys required; `false` when not immune (ADR-001 §7)
- `hitDice` — format `NdX` or `NdX+Y` (e.g., `2d6`, `23d8+92`) (ADR-001 §9)
- `speeds` — all values in **feet** (integers); `null` when the movement type does not apply; always present, never omitted
- `translations.{locale}.name` — when multiple official names exist, separate with ` / `. Never use `|` as a separator.
- `translations.{locale}.subtype` — locale-specific string or `null`; always present, never omitted
- `translations.{locale}.senses` — raw locale text; EN uses feet, FR uses rounded metres
- `translations.{locale}.languages` — array of strings (locale-specific names)
- `translations.{locale}.description` — GFM Markdown containing traits, actions, reactions, and legendary actions; use `|-` block scalar (strip chomping, no trailing newline)

### Empty Descriptions

When a description field is empty after parsing the source:

- **One locale missing** — translate from the available locale. Both `en` and `fr` are required; do not leave a description field empty.
- **Both locales missing** — stop and ask the user to provide an alternative source (e.g. wikidot, D&D Beyond, official PDF) before writing the file.

### Monster Checklist

- [ ] `id` — slugified English name (e.g., `"goblin"`, `"young-red-dragon"`)
- [ ] `source` — source string from known values
- [ ] `type` — see [Creature Types](srd-fr-en.md#creature-types)
- [ ] `size` — see [Creature Sizes](srd-fr-en.md#creature-sizes)
- [ ] `alignment` — see [Alignments](srd-fr-en.md#alignments)
- [ ] `challengeRating` — float (e.g., `0.25`, `0.5`, `1.0`, `20.0`)
- [ ] `armorClass` — integer only (no armor type string)
- [ ] `maxHitPoints` — integer (average)
- [ ] `hitDice` — string (e.g., `2d6`, `23d8+92`)
- [ ] `abilities` — all 6 keys; integer score
- [ ] `savingThrows` — map of proficient/expert abilities only; omit entirely if none
- [ ] `speeds` — all 6 keys (`walk`, `fly`, `swim`, `climb`, `burrow`, `hover`); values in feet or `null`
- [ ] `skills` — all 18 keys present
- [ ] `damageAffinities` — all 16 keys present
- [ ] `conditionImmunities` — all 14 keys present
- [ ] `translations.{locale}.name`
- [ ] `translations.{locale}.subtype` — locale-specific string or `null`
- [ ] `translations.{locale}.description` — GFM Markdown (traits, actions, reactions, legendary actions)
- [ ] `translations.{locale}.senses` — raw text
- [ ] `translations.{locale}.languages` — array of strings

---

## Damage Affinity Values

| Key            | Meaning             |
|----------------|---------------------|
| `none`         | No special affinity |
| `resistant`    | Takes half damage   |
| `immune`       | Takes no damage     |
| `vulnerable`   | Takes double damage |

The 16 damage type keys: `acid`, `bludgeoning`, `cold`, `fire`, `force`, `lightning`,
`necrotic`, `piercing`, `poison`, `psychic`, `radiant`, `slashing`, `thunder`,
`bludgeoningNonMagical`, `piercingNonMagical`, `slashingNonMagical`.

---

## Validation Rules

- **No unknown fallback without documentation**: if a source value has no matching entry in the translation map, document it in [`srd-fr-en.md`](srd-fr-en.md) before adding it to the data file.
- **Ability scores must be integers**: reject `"15 (+2)"` — store `15` only.
- **AC must be an integer**: reject `"11 (armure de cuir)"` — store `11` only.
- **Senses belong in `translations`**: never store them as locale-independent data.
- **Languages belong in `translations`**: never store language names as locale-independent data.
- **No pre-computed modifiers**: store scores and proficiency levels; let the app compute bonuses.
- **All bounded objects must be exhaustive**: `skills` (18 keys), `damageAffinities` (16 keys), `conditionImmunities` (14 keys) — all keys always present.
- **Run the build script** after any change: `python scripts/build_compendium.py`

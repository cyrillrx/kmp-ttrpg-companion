# SRD Spell Ingestion Guide

Guide for contributors adding or migrating spell data into the normalized YAML source format.

For the complete FR↔EN term mapping, see [`docs/data/srd-fr-en.md`](srd-fr-en.md).  
For the data model decisions behind these formats, see [`docs/adr/adr-001-data-model.md`](../adr/adr-001-data-model.md).  
For the source format decision, see [`docs/adr/adr-002-compendium-source-format.md`](../adr/adr-002-compendium-source-format.md).

> **Source files are in `data/compendium/spells/*.yaml`.**  
> The JSON files (`data/compendium/spells.json`, `composeResources/files/spells.json`) are **generated** — run `scripts/build_compendium.py` after editing. Do not edit the JSON files directly.

---

## General Rules

- All enum values use **lowercase English with underscores** (e.g., `"conjuration"`, `"evocation"`).
- All keys are in English. French content belongs only inside `translations.fr`.
- The `source` field identifies the origin of the entry — see [known source values](../adr/adr-001-data-model.md#2-source-field).
- An entity with an empty `translations` map is considered malformed.
- Descriptions are currently **HTML** (Phase 1). Migration to GFM Markdown is deferred to Phase 2, alongside the Compose renderer update.

---

## Spell Entry Format

```yaml
id: acid-splash
source: srd_5.1
level: 0
school: conjuration
concentration: false
ritual: false
components:
  verbal: true
  somatic: true
  material: false
availableClasses:
  - sorcerer
  - wizard
translations:
  en:
    name: Acid Splash
    castingTime: 1 action
    range: 60 feet
    duration: Instantaneous
    materialDescription: null
    description: |-
      You hurl a bubble of acid. Choose one or two creatures you can see within range.
      If you choose two, they must be within 5 feet of each other.
      A target must succeed on a Dexterity saving throw or take **1d6 acid damage**.
  fr:
    name: Aspersion acide
    castingTime: 1 action
    range: 18 mètres
    duration: instantanée
    materialDescription: null
    description: |-
      Vous lancez une bulle d'acide. Choisissez une ou deux créatures visibles dans la portée.
      Si vous en choisissez deux, elles doivent se trouver à 1,50 mètre l'une de l'autre.
```

### Notes

- `level` — `0` for cantrips
- `availableClasses` — see [Character Classes](srd-fr-en.md#character-classes)
- `translations.{locale}.name` — when multiple official names exist (e.g. old and new edition), separate them with ` / ` (e.g. `"Immobilisation une personne / un humanoïde"`). Never use `|` as a separator.
- `translations.{locale}.range` — locale-specific raw text (English uses feet, French uses metres)
- `translations.{locale}.materialDescription` — string or `null`; always present, never omitted
- `translations.{locale}.description` — HTML (Phase 1); use `|-` block scalar (strip chomping, no trailing newline)

### Empty Descriptions

When a description field is empty after parsing the source:

- **One locale missing** — translate from the available locale. Both `en` and `fr` are required; do not leave a description field empty.
- **Both locales missing** — stop and ask the user to provide an alternative source (e.g. wikidot, D&D Beyond, official PDF) before writing the file.

### Spell Checklist

- [ ] `id` — slugified English name (e.g., `"acid-splash"`, `"fireball"`)
- [ ] `source` — source string from known values
- [ ] `level` — integer, `0` for cantrips
- [ ] `school` — see [Spell Schools](srd-fr-en.md#spell-schools)
- [ ] `concentration` — boolean
- [ ] `ritual` — boolean
- [ ] `components.verbal`, `components.somatic`, `components.material` — booleans
- [ ] `availableClasses` — array of class keys
- [ ] `translations.{locale}.name`
- [ ] `translations.{locale}.castingTime`
- [ ] `translations.{locale}.range`
- [ ] `translations.{locale}.duration`
- [ ] `translations.{locale}.materialDescription` — string or `null`
- [ ] `translations.{locale}.description` — HTML

---

## Validation Rules

- **No unknown fallback without documentation**: if a source value has no matching entry in the translation map, document it in [`srd-fr-en.md`](srd-fr-en.md) before adding it to the data file.
- **No pre-computed modifiers**: store levels and flags; let the app compute display strings.
- **Run the build script** after any change: `python scripts/build_compendium.py`

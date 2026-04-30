# SRD Spell Ingestion Guide

Guide for contributors adding or migrating spell data into the normalized JSON format.

For the complete FR↔EN term mapping, see [`docs/i18n/srd-fr-en.md`](../docs/i18n/srd-fr-en.md).  
For the data model decisions behind these formats, see [`docs/adr/ADR-001-data-model.md`](../docs/adr/ADR-001-data-model.md).

---

## General Rules

- All enum values use **lowercase English with underscores** (e.g., `"conjuration"`, `"evocation"`).
- All keys are in English. French content belongs only inside `translations.fr`.
- The `source` field identifies the origin of the entry — see [known source values](../docs/adr/ADR-001-data-model.md#2-source-field).
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
    "en": {
      "name": "Acid Splash",
      "castingTime": "1 action",
      "range": "60 feet",
      "duration": "Instantaneous",
      "materialDescription": null,
      "description": "<p>...</p>"
    },
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

### Notes

- `level` — `0` for cantrips
- `availableClasses` — see [Character Classes](../docs/i18n/srd-fr-en.md#character-classes)
- `translations.{locale}.range` — locale-specific raw text (English uses feet, French uses metres)
- `translations.{locale}.materialDescription` — string or `null`

### Spell Checklist

- [ ] `id` — slugified English name (e.g., `"acid-splash"`, `"fireball"`)
- [ ] `source` — source string from known values
- [ ] `level` — integer, `0` for cantrips
- [ ] `school` — see [Spell Schools](../docs/i18n/srd-fr-en.md#spell-schools)
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

- **No unknown fallback without documentation**: if a source value has no matching entry in the translation map, document it in [`srd-fr-en.md`](../docs/i18n/srd-fr-en.md) before adding it to the data file.
- **No pre-computed modifiers**: store levels and flags; let the app compute display strings.

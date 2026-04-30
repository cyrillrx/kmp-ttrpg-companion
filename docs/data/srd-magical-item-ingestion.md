# SRD Magical Item Ingestion Guide

Guide for contributors adding or migrating magical item data into the normalized JSON format.

For the complete FR↔EN term mapping, see [`docs/i18n/srd-fr-en.md`](../docs/i18n/srd-fr-en.md).  
For the data model decisions behind these formats, see [`docs/adr/ADR-001-data-model.md`](../docs/adr/ADR-001-data-model.md).

---

## General Rules

- All enum values use **lowercase English with underscores** (e.g., `"wondrous_item"`, `"uncommon"`).
- All keys are in English. French content belongs only inside `translations.fr`.
- The `source` field identifies the origin of the entry — see [known source values](../docs/adr/ADR-001-data-model.md#2-source-field).
- An entity with an empty `translations` map is considered malformed.

---

## Magical Item Entry Format

```json
{
  "id": "shield-plus-1",
  "source": "srd_5.1",
  "type": "armor",
  "rarity": "uncommon",
  "attunement": false,
  "translations": {
    "en": {
      "name": "Shield, +1",
      "subtype": "Shield",
      "description": "<p>...</p>"
    },
    "fr": {
      "name": "Bouclier +1",
      "subtype": "Bouclier",
      "description": "<p>...</p>"
    }
  }
}
```

### Notes

- `attunement`: any non-null French source string (e.g., `"harmonisation requise"`) maps to `true`
- `translations.{locale}.subtype` — optional locale-specific weapon/armor sub-category
- The display subtitle (`"Objet merveilleux · Peu courant · Harmonisation"`) is computed by the client from typed fields — not stored

### Item Checklist

- [ ] `id` — slugified English name
- [ ] `source` — source string from known values
- [ ] `type` — see [Magical Item Types](../docs/i18n/srd-fr-en.md#magical-item-types)
- [ ] `rarity` — see [Magical Item Rarities](../docs/i18n/srd-fr-en.md#magical-item-rarities)
- [ ] `attunement` — boolean
- [ ] `translations.{locale}.name`
- [ ] `translations.{locale}.subtype` — optional
- [ ] `translations.{locale}.description` — HTML

---

## Validation Rules

- **No unknown fallback without documentation**: if a source value has no matching entry in the translation map, document it in [`srd-fr-en.md`](../docs/i18n/srd-fr-en.md) before adding it to the data file.

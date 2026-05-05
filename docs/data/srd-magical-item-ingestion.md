# SRD Magical Item Ingestion Guide

Guide for contributors adding or migrating magical item data into the normalized YAML source format.

For the complete FR↔EN term mapping, see [`docs/data/srd-fr-en.md`](srd-fr-en.md).  
For the data model decisions behind these formats, see [`docs/adr/adr-001-data-model.md`](../adr/adr-001-data-model.md).  
For the source format decision, see [`docs/adr/adr-002-compendium-source-format.md`](../adr/adr-002-compendium-source-format.md).

> **Source files are in `data/compendium/magical-items/*.yaml`.**  
> The JSON files (`data/compendium/magical-items.json`, `composeResources/files/magical-items.json`) are **generated** — run `scripts/build_compendium.py` after editing. Do not edit the JSON files directly.

---

## General Rules

- All enum values use **lowercase English with underscores** (e.g., `"wondrous_item"`, `"uncommon"`).
- All keys are in English. French content belongs only inside `translations.fr`.
- The `source` field identifies the origin of the entry — see [known source values](../adr/adr-001-data-model.md#2-source-field).
- An entity with an empty `translations` map is considered malformed.
- Descriptions are currently **HTML** (Phase 1). Migration to GFM Markdown is deferred to Phase 2, alongside the Compose renderer update.

---

## Magical Item Entry Format

```yaml
id: shield-plus-1
source: srd_5.1
type: armor
rarity: uncommon
attunement: false
translations:
  en:
    name: "Shield, +1"
    subtype: Shield
    description: |
      While holding this shield, you have a +1 bonus to AC.
      This bonus is in addition to the shield's normal bonus to AC.
  fr:
    name: Bouclier +1
    subtype: Bouclier
    description: |
      Lorsque vous tenez ce bouclier, vous bénéficiez d'un bonus de +1 à la CA.
      Ce bonus s'ajoute au bonus normal du bouclier à la CA.
```

### Notes

- `attunement` — boolean; any non-null French source string (e.g., `"harmonisation requise"`) maps to `true`
- `translations.{locale}.subtype` — optional locale-specific weapon/armor sub-category; always present, set to `null` if absent
- `translations.{locale}.name` — when multiple official names exist, separate with ` / ` (e.g. `"Selle du cavalier / du hussard"`). Never use `|` as a separator.
- `translations.{locale}.description` — HTML (Phase 1)
- The display subtitle (`"Objet merveilleux · Peu courant · Harmonisation"`) is computed by the client from typed fields — not stored

### Item Checklist

- [ ] `id` — slugified English name
- [ ] `source` — source string from known values
- [ ] `type` — see [Magical Item Types](srd-fr-en.md#magical-item-types)
- [ ] `rarity` — see [Magical Item Rarities](srd-fr-en.md#magical-item-rarities)
- [ ] `attunement` — boolean
- [ ] `translations.{locale}.name`
- [ ] `translations.{locale}.subtype` — locale-specific string or `null`
- [ ] `translations.{locale}.description` — HTML (Phase 1)

---

## Validation Rules

- **No unknown fallback without documentation**: if a source value has no matching entry in the translation map, document it in [`srd-fr-en.md`](srd-fr-en.md) before adding it to the data file.
- **Run the build script** after any change: `python scripts/build_compendium.py`

# PRD-001 — Reference Data (Spells, Items, Monsters)

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-03-15

## Overview

Three reference modules — **Spellbook**, **Item List**, and **Bestiary** — allow users to browse, search, and filter the official D&D 5e content catalog. They share the same core pattern: a searchable, filterable list leading to a detail view, with CRUD for custom entries.

These modules are **game-system-specific** (D&D 5e). For system-agnostic content like personal notes, see [PRD-004 — Notes](PRD-004-NOTES.md).

## Goals

- Give every user fast access to the full rules reference during a session.
- Allow all users to organize content into personal named lists of the same type.
- Support offline access from day one.

> **Scope note**: At this stage, all entities are independent. Lists can only group entities of the **same type** (e.g. a grimoire = a named list of spells). Cross-entity relationships — such as linking a spell to a character sheet or attaching items to a campaign — will be addressed in a later phase.

## Shared Pattern

All three modules follow the same structure:

```
[Module]List
├── Search bar (full-text)
├── Filter panel (module-specific filters)
└── [Module]Detail
```

---

## 1. Spellbook

### Filters
- Spell level (0–9)
- School of magic (Abjuration, Conjuration, Divination, Enchantment, Evocation, Illusion, Necromancy, Transmutation)
- Casting class (Wizard, Cleric, Druid, etc.)

### User Stories

| As a… | I want to…                                      | So that…                                                   |
|-------|-------------------------------------------------|------------------------------------------------------------|
| User  | Browse and search the full spell list           | I can quickly look up a spell during a session             |
| User  | Filter spells by level and school               | I can narrow down relevant options                         |
| User  | Add a spell to a personal named list (grimoire) | I can organize spells for a specific character or campaign |
| User  | Create a custom spell                           | I can use homebrew content                                 |
| User  | Export a spell / import a shared spell          | I can exchange homebrew content with others                |

### Functional Requirements

**Phase 1 — MVP (Offline)**
- Display a scrollable list of all spells from the local seed database.
- Full-text search on name and description.
- Filter by level and school of magic.
- View spell detail (name, level, school, casting time, range, components, duration, description).

**Phase 2 — Online Public Data**
- Sync the spell catalog from the public backend (read-only).

**Phase 3 — Advanced**
- Create, edit, and delete personal named spell lists (grimoires).
- Create, edit, and delete custom spells.
- Export a spell as a shareable format; import a spell shared by another user.

---

## 2. Item List

### Filters
- Item type (Weapon, Armor, Potion, Wondrous Item, etc.)
- Rarity (Common, Uncommon, Rare, Very Rare, Legendary, Artifact)

### User Stories

| As a… | I want to…                              | So that…                                     |
|-------|-----------------------------------------|----------------------------------------------|
| User  | Browse and search the full item catalog | I can look up item stats and descriptions    |
| User  | Filter items by type and rarity         | I can find relevant loot for my session      |
| User  | Add items to a personal named list      | I can track loot or prepare a shop inventory |
| User  | Create a custom item                    | I can use homebrew items                     |
| User  | Export and import items                 | I can share custom items with others         |

### Functional Requirements

Same structure as Spellbook. Filters specific to items as listed above.

Item detail includes: name, type, rarity, description, weight, value, attunement requirement.

---

## 3. Bestiary

### Filters
- Monster type (Beast, Undead, Humanoid, Dragon, Fiend, etc.)
- Challenge Rating (CR 0 to CR 30)

### User Stories

| As a… | I want to…                                 | So that…                          |
|-------|--------------------------------------------|-----------------------------------|
| User  | Browse and search the full monster catalog | I can look up stat blocks quickly |
| User  | Filter by type and CR                      | I can find appropriate encounters |
| User  | Create a custom monster or NPC             | I can use homebrew creatures      |
| User  | Add monsters to a personal list            | I can prepare an encounter roster |
| User  | Export and import monsters                 | I can share custom creatures      |

### Functional Requirements

Same structure as Spellbook. Filters specific to monsters as listed above.

Monster detail includes: name, type, size, alignment, AC, HP, speed, ability scores, skills, senses, languages, CR, traits, actions, reactions, legendary actions.

## Non-Functional Requirements

- List screens must render at least 200 items smoothly with search/filter applied in under 300ms.
- All data must be available offline (seeded locally on first launch).
- Custom entries must be clearly distinguished from official content in the UI.

## Out of Scope

- Interactive spell slot tracking or combat automation.
- Automatic CR balancing for encounter building (may be addressed in a future PRD).

## Open Questions

- Which official data source will be used for the seed database (SRD JSON, Open5e API, manual curation)?
- Should personal named lists be shareable between users (e.g., sharing a grimoire with a party)?

# PRD-001 — Reference Data (Overview)

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-03-15

## Overview

Three reference modules allow users to browse, search, and filter the official D&D 5e content catalog. These modules are **game-system-specific** (D&D 5e). For system-agnostic content like personal notes, see [PRD-004 — Notes](PRD-004-NOTES.md).

| Module    | PRD                                           |
|-----------|-----------------------------------------------|
| Spellbook | [PRD-001a — Spellbook](PRD-001a-SPELLBOOK.md) |
| Item List | [PRD-001b — Item List](PRD-001b-ITEM-LIST.md) |
| Bestiary  | [PRD-001c — Bestiary](PRD-001c-BESTIARY.md)   |

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

And the same phase breakdown:

- **Phase 1**: Seed database, search, filters, detail view.
- **Phase 2**: Create and manage personal named lists locally.
- **Phase 3**: Sync catalog from public backend (read-only).
- **Phase 4**: Sync personal lists across devices. Custom entries, export/import.

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

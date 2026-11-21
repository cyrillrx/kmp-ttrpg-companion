# PRD-001: Reference Data (Overview)

**Status**: Draft  
**Version**: 0.2  
**Last updated**: 2026-11-14

## Overview

Three reference modules allow users to browse, search, and filter the official D&D 5e content catalog. These modules are **game-system-specific** (D&D 5e). For system-agnostic content like personal notes, see [PRD-004 — Notes](prd-004-notes.md).

| Module    | PRD                                           |
|-----------|-----------------------------------------------|
| Spellbook | [PRD-001a — Spellbook](prd-001a-spellbook.md) |
| Item List | [PRD-001b — Item List](prd-001b-item-list.md) |
| Bestiary  | [PRD-001c — Bestiary](prd-001c-bestiary.md)   |

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
- **Phase 3**: Custom entries. Export and import single entries, then personal lists; a list export carries its entries in full, not references, so the file stays valid whatever compendium version the receiver has and carries custom entries; it builds on the entry export. Mixing references for official entries with full data for custom ones was rejected: it ties the file to the receiver's compendium version. The cost: the receiver gets copies of official entries it already has, possibly of another version, and how they relate is for the dedicated PRD to write that scopes both exports.
- **Online**: Sync personal lists and custom entries across devices. The catalog itself stays on the device.

## Non-Functional Requirements

- List screens must render at least 200 items smoothly with search/filter applied in under 300ms.
- All data must be available offline (seeded locally on first launch).
- Custom entries must be clearly distinguished from official content in the UI.

## Out of Scope

- Interactive spell slot tracking or combat automation.
- Automatic CR balancing for encounter building (may be addressed in a future PRD).

## Open Questions

- Which official data source will be used for the seed database (SRD JSON, Open5e API, manual curation)?
- Should personal named lists be shareable between users online (e.g., sharing a grimoire with a party)? Offline sharing by file is settled in Phase 3 above.

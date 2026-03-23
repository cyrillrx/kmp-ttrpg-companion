# PRD-001a — Spellbook

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-03-15

Refer to [PRD-001 — Reference Data](PRD-001-REFERENCE-DATA.md) for the shared pattern, goals, and non-functional requirements.

## Filters

- Spell level (0–9)
- School of magic (Abjuration, Conjuration, Divination, Enchantment, Evocation, Illusion, Necromancy, Transmutation)
- Casting class (Wizard, Cleric, Druid, Bard, Paladin, Ranger, Sorcerer, Warlock…)

## User Stories

| As a… | I want to…                                      | So that…                                                   |
|-------|-------------------------------------------------|------------------------------------------------------------|
| User  | Browse and search the full spell list           | I can quickly look up a spell during a session             |
| User  | Filter spells by level, school, and class       | I can narrow down relevant options                         |
| User  | Add a spell to a personal named list (grimoire) | I can organize spells for a specific character or campaign |
| User  | Create a custom spell                           | I can use homebrew content                                 |
| User  | Export a spell / import a shared spell          | I can exchange homebrew content with others                |

## Functional Requirements

### Phase 1 — MVP (Offline)

- [x] Display a scrollable list of all spells from the local seed database.
- [x] Full-text search on name and description.
- [x] Filter by level, school of magic, and casting class.
- [x] View spell detail: name, level, school, casting time, range, components, duration, description.

### Phase 2 — Local Lists

- [ ] Create, edit, and delete personal named spell lists (grimoires) stored locally.

### Phase 3 — Online Public Data

- [ ] Sync the spell catalog from the public backend (read-only).

### Phase 4 — Advanced

- [ ] Sync personal grimoires across devices.
- [ ] Create, edit, and delete custom spells.
- [ ] Export a spell as a shareable format; import a spell shared by another user.

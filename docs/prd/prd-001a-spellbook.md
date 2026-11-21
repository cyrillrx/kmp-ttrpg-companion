# PRD-001a: Spellbook

**Status**: Draft  
**Version**: 0.2  
**Last updated**: 2026-11-14

Refer to [PRD-001 — Reference Data](prd-001-reference-data.md) for the shared pattern, goals, and non-functional requirements.

## Filters

- Spell level (0–9)
- School of magic (Abjuration, Conjuration, Divination, Enchantment, Evocation, Illusion, Necromancy, Transmutation)
- Casting class (Wizard, Cleric, Druid, Bard, Paladin, Ranger, Sorcerer, Warlock…)

## User Stories

| As a… | I want to…                                      | So that…                                                   |
|-------|-------------------------------------------------|------------------------------------------------------------|
| User  | Browse and search the full spell list           | I can quickly look up a spell during a session             |
| User  | Filter spells by level, school, and class       | I can narrow down relevant options                         |
| User  | Add a spell to a personal named collection      | I can organize spells for a specific character or campaign |
| User  | Create a custom spell                           | I can use homebrew content                                 |
| User  | Export a spell / import a shared spell          | I can exchange homebrew content with others                |

## Functional Requirements

### Phase 1 — MVP (Offline)

- [x] Display a scrollable list of all spells from the local seed database.
- [x] Full-text search on name and description.
- [x] Filter by level, school of magic, and casting class.
- [x] View spell detail: name, level, school, casting time, range, components, duration, description.

### Phase 2 — Local Collections

- [x] Create, edit, and delete personal named collections stored locally.

### Phase 3: Custom entries, export and import

- [ ] Create, edit, and delete custom spells.
- [ ] Export a spell as a shareable format; import a spell shared by another user.
- [ ] Export and import a personal collection, carrying its entries in full (dedicated PRD to write, see [PRD-001](prd-001-reference-data.md)).

### Online

> The catalog itself stays on the device (see [PRD-000](prd-000-vision.md)).

- [ ] Sync personal collections across devices.
- [ ] Sync custom spells across devices.

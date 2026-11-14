# PRD-001c — Bestiary

> **Status**: Draft | **Version**: 0.2 | **Last updated**: 2026-11-14

Refer to [PRD-001 — Reference Data](prd-001-reference-data.md) for the shared pattern, goals, and non-functional requirements.

## Filters

- Monster type (Beast, Undead, Humanoid, Dragon, Fiend, Construct, Elemental, etc.)
- Challenge Rating (CR 0 to CR 30)

## User Stories

| As a… | I want to…                                 | So that…                          |
|-------|--------------------------------------------|-----------------------------------|
| User  | Browse and search the full monster catalog | I can look up stat blocks quickly |
| User  | Filter by type and CR                      | I can find appropriate encounters |
| User  | Create a custom monster or NPC             | I can use homebrew creatures      |
| User  | Add monsters to a personal list            | I can prepare an encounter roster |
| User  | Export and import monsters                 | I can share custom creatures      |

## Functional Requirements

### Phase 1 — MVP (Offline)

- [x] Display a scrollable list of all monsters from the local seed database.
- [x] Full-text search on name and description.
- [x] Filter by monster type and CR.
- [x] View monster detail: name, type, size, alignment, AC, HP, speed, ability scores, skills, senses, languages, CR, traits, actions, reactions, legendary actions.

### Phase 2 — Local Lists

- [x] Create, edit, and delete personal named monster lists stored locally.

### Phase 3: Custom entries, export and import

- [ ] Create, edit, and delete custom monsters and NPCs.
- [ ] Export a monster as a shareable format; import a monster shared by another user.
- [ ] Export and import a personal collection, carrying its entries in full (dedicated PRD to write, see [PRD-001](prd-001-reference-data.md)).

### Online

> The catalog itself stays on the device (see [PRD-000](prd-000-vision.md)).

- [ ] Sync personal monster lists across devices.
- [ ] Sync custom monsters and NPCs across devices.

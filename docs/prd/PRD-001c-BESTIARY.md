# PRD-001c — Bestiary

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-03-15

Refer to [PRD-001 — Reference Data](PRD-001-REFERENCE-DATA.md) for the shared pattern, goals, and non-functional requirements.

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

- Display a scrollable list of all monsters from the local seed database.
- Full-text search on name and description.
- Filter by monster type and CR.
- View monster detail: name, type, size, alignment, AC, HP, speed, ability scores, skills, senses, languages, CR, traits, actions, reactions, legendary actions.

### Phase 2 — Local Lists

- Create, edit, and delete personal named monster lists stored locally.

### Phase 3 — Online Public Data

- Sync the monster catalog from the public backend (read-only).

### Phase 4 — Advanced

- Sync personal monster lists across devices.
- Create, edit, and delete custom monsters and NPCs.
- Export a monster as a shareable format; import a monster shared by another user.

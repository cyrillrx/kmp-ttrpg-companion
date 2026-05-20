# Roadmap

This document tracks the macro vision and planned phases for the TTRPG Companion app.
For detailed feature specifications, see the [PRDs](prd/).

## Phase 1 - Cleanup + `HomeScreen` revamp

> Resolve open design decisions before moving forward.

- [x] Decide on Spellbook layout (vertical list vs. horizontal cards) and remove the alternative
- [x] Remove the duplicate Spellbook button from `HomeScreen`

## Phase 2 - Browsing & Filtering - Reference Data

> Bring current implementations to a production-ready state. - [PRD-001](prd/prd-001-reference-data.md)

- [x] Complete Spellbook filters (school, class) - [PRD-001a](prd/prd-001a-spellbook.md)
- [x] Complete Magical item filters (type, rarity) - [PRD-001b](prd/prd-001b-item-list.md)
- [x] Complete Bestiary filters (type, CR) - [PRD-001c](prd/prd-001c-bestiary.md)

## Phase 3 - Local Lists

> Allow users to build and manage their own lists offline. - [PRD-001](prd/prd-001-reference-data.md)

*Requires: Reference Data - Browsing & Filtering*

- [x] Local spell lists - [PRD-001a](prd/prd-001a-spellbook.md)
- [x] Local item lists - [PRD-001b](prd/prd-001b-item-list.md)
- [x] Local bestiary lists - [PRD-001c](prd/prd-001c-bestiary.md)

## Phase 4 - Backend setup and sync engine

> Fetch public reference data from the server and sync user lists. - [PRD-001](prd/prd-001-reference-data.md)

- [x] Setup backend service with a simple API
- [ ] Data quality & coherence — DB-ready models, i18n, source tracking
  - [x] Architecture decisions & SRD translation map ([ADR-001](adr/adr-001-data-model.md), [Translation Map](data/srd-fr-en.md))
  - [x] Spells: structured components, concentration/ritual flags, complete class list
  - [x] Magical items: typed fields normalized
  - [x] Creatures: full stat block (speed, senses, skills, saving throws, damage affinities); character model coherence
- [ ] Fetch public reference data from backend (spells, items, creatures)
- [ ] Develop the sync engine on which will rely all sync logic and conflict resolution
- [ ] Sync user lists with backend

## Phase 5 - Character Sheets

> Allow users to create and manage character sheets. - [PRD-002](prd/prd-002-character-sheet.md)

- [ ] Create, list and edit character sheets
  - [x] Basic character info (name, race, class, background, alignment)
  - [x] Ability scores
  - [ ] Combat stats
  - [ ] Speed (base speed, in feets and meters)
  - [ ] Skills
  - [ ] Combat actions
  - [ ] Inventory
- [x] Persist `Character` with SQLDelight (currently RAM only)
- [ ] Sync character sheets with backend

## Phase 6 - Notes

> Create and manage system-agnostic notes. - [PRD-004](prd/prd-004-notes.md)

- [ ] Create, list and edit notes
- [ ] Create lists of notes
- [ ] Sync notes with backend

## Phase 7 - Campaigns

> Connect entities together through campaigns. - [PRD-003](prd/prd-003-campaigns.md)

*Requires: Notes - Local implementation*

- [ ] Create, list and edit campaigns
- [ ] Attach player to campaigns
- [ ] Attach notes to campaigns
- [ ] Sync campaigns with backend
- [ ] Campaign roles (GM, Co-GM, Player)
- [ ] Share campaigns with other users
- [ ] Note visibility / Secret folders (GM vs. players)

## Phase 8 - Advanced Notes

> Allow notes to reference other entities (characters, creatures, items, other notes).

- [ ] Link notes to other entities (simple reference or inline)

## Standalone Features

> Independent features with no hard dependencies on other phases.

- [ ] Dice roller - [PRD-006](prd/prd-006-dice.md)
- [ ] Generator gallery (curated tables) - [PRD-005](prd/prd-005-generators.md)
- [ ] AI-based generators - feasibility study required [PRD-005](prd/prd-005-generators.md)

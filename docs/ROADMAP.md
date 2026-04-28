# Roadmap

This document tracks the macro vision and planned phases for the TTRPG Companion app.
For detailed feature specifications, see the [PRDs](prd/).

## Phase 1 - Cleanup + `HomeScreen` revamp

> Resolve open design decisions before moving forward.

- [x] Decide on Spellbook layout (vertical list vs. horizontal cards) and remove the alternative
- [x] Remove the duplicate Spellbook button from `HomeScreen`

## Phase 2 - Browsing & Filtering - Reference Data

> Bring current implementations to a production-ready state. - [PRD-001](prd/PRD-001-REFERENCE-DATA.md)

- [x] Complete Spellbook filters (school, class) - [PRD-001a](prd/PRD-001a-SPELLBOOK.md)
- [x] Complete Magical item filters (type, rarity) - [PRD-001b](prd/PRD-001b-ITEM-LIST.md)
- [x] Complete Bestiary filters (type, CR) - [PRD-001c](prd/PRD-001c-BESTIARY.md)

## Phase 3 - Local Lists

> Allow users to build and manage their own lists offline. - [PRD-001](prd/PRD-001-REFERENCE-DATA.md)

*Requires: Reference Data - Browsing & Filtering*

- [x] Local spell lists - [PRD-001a](prd/PRD-001a-SPELLBOOK.md)
- [x] Local item lists - [PRD-001b](prd/PRD-001b-ITEM-LIST.md)
- [x] Local bestiary lists - [PRD-001c](prd/PRD-001c-BESTIARY.md)

## Phase 4 - Backend setup and sync engine

> Fetch public reference data from the server and sync user lists. - [PRD-001](prd/PRD-001-REFERENCE-DATA.md)

- [x] Setup backend service with a simple API
- [ ] Data quality & coherence — DB-ready models, i18n, source tracking
  - [x] Architecture decisions & SRD translation map ([ADR-001](adr/ADR-001-data-model.md), [i18n](i18n/srd-fr-en.md))
  - [ ] Spells: structured components, concentration/ritual flags, complete class list
  - [ ] Magical items: typed fields normalized
  - [ ] Creatures: full stat block (speed, senses, skills, saving throws, damage affinities); character model coherence
- [ ] Fetch public reference data from backend (spells, items, creatures)
- [ ] Develop the sync engine on which will rely all sync logic and conflict resolution
- [ ] Sync user lists with backend

## Phase 5 - Character Sheets

> Allow users to create and manage character sheets. - [PRD-002](prd/PRD-002-CHARACTER-SHEET.md)

- [ ] Create, list and edit character sheets
- [ ] Persist `PlayerCharacter` with SQLDelight (currently RAM only)
- [ ] Sync character sheets with backend

## Phase 6 - Notes

> Create and manage system-agnostic notes. - [PRD-004](prd/PRD-004-NOTES.md)

- [ ] Create, list and edit notes
- [ ] Create lists of notes
- [ ] Sync notes with backend

## Phase 7 - Campaigns

> Connect entities together through campaigns. - [PRD-003](prd/PRD-003-CAMPAIGNS.md)

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

- [ ] Dice roller - [PRD-006](prd/PRD-006-DICE.md)
- [ ] Generator gallery (curated tables) - [PRD-005](prd/PRD-005-GENERATORS.md)
- [ ] AI-based generators - feasibility study required [PRD-005](prd/PRD-005-GENERATORS.md)

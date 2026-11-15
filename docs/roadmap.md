# Roadmap

This document tracks the macro vision and planned phases for the TTRPG Companion app.
For detailed feature specifications, see the [PRDs](prd/).

## ✅ Phase 1 - Cleanup + `HomeScreen` revamp

> Resolve open design decisions before moving forward.

- [x] Decide on Spellbook layout (vertical list vs. horizontal cards) and remove the alternative
- [x] Remove the duplicate Spellbook button from `HomeScreen`

## ✅ Phase 2 - Browsing & Filtering - Reference Data

> Bring current implementations to a production-ready state. - [PRD-001](prd/prd-001-reference-data.md)

- [x] Complete Spellbook filters (school, class) - [PRD-001a](prd/prd-001a-spellbook.md)
- [x] Complete Magical item filters (type, rarity) - [PRD-001b](prd/prd-001b-item-list.md)
- [x] Complete Bestiary filters (type, CR) - [PRD-001c](prd/prd-001c-bestiary.md)

## ✅ Phase 3 - Collections

> Allow users to build and manage their own collections offline. - [PRD-001](prd/prd-001-reference-data.md)

*Requires: Reference Data - Browsing & Filtering*

- [x] Local spell collections - [PRD-001a](prd/prd-001a-spellbook.md)
- [x] Local item collections - [PRD-001b](prd/prd-001b-item-list.md)
- [x] Local bestiary collections - [PRD-001c](prd/prd-001c-bestiary.md)

## Phase 4 - Compendium data quality

> Make the bundled compendium coherent and DB-ready: structured models, i18n, source tracking. - [ADR-001](adr/adr-001-data-model.md)

- [x] Architecture decisions & SRD translation map ([ADR-001](adr/adr-001-data-model.md), [Translation Map](data/srd-fr-en.md))
- [x] Spells: structured components, concentration/ritual flags, complete class list
- [x] Magical items: typed fields normalized
- [x] Creatures: full stat block (speed, senses, skills, saving throws, damage affinities); character model coherence
- [ ] Descriptions: handle the source links (aidedd) carried in imported text

## Phase 5 - Character Sheets

> Allow users to create and manage character sheets. - [PRD-002](prd/prd-002-character-sheet.md)

- [ ] Create, list and edit character sheets
  - [x] Basic character info (name, race, class, background, alignment)
  - [x] Ability scores
  - [x] Combat stats
    - [x] Max hit points
    - [x] Armor class
    - [x] Initiative
    - [x] Speed (base speed, in feet and meters)
    - [x] Current hit points
  - [x] Skills
  - [ ] Combat actions
  - [ ] Currency and carrying capacity
  - [ ] Collections attached to the character - PRD to write, after the collection export (Standalone Features); ships with their export in the sheet ([PRD-007](prd/prd-007-character-import-export.md))
    - [ ] Inventory (items)
    - [ ] Spells
    - [ ] Wild shapes (creatures)
  - [ ] Mundane equipment (weapons, armor, adventuring gear) - open question in [PRD-002](prd/prd-002-character-sheet.md)
- [x] Persist `Character` with SQLDelight
- [x] Import and export a character sheet - [PRD-007](prd/prd-007-character-import-export.md)
- [x] Let the user order the character sheet list by name or by last modified (#256), remembered between launches ([ADR-005](adr/adr-005-list-order-preferences.md))
- [ ] Expand the PC and NPC preset gallery

## Phase 6 - Campaigns

> Gather the characters of a campaign, PCs and NPCs, in one place. - [PRD-003](prd/prd-003-campaigns.md)

- [ ] Create, list and edit campaigns
- [ ] Attach characters (PCs and NPCs) to a campaign

## Phase 7 - Notes

> Create and manage system-agnostic notes, then connect them to campaigns and other entities. - [PRD-004](prd/prd-004-notes.md)

- [ ] Create, list and edit notes
- [ ] Create lists of notes
- [ ] Attach notes to campaigns
- [ ] Link notes to other entities (characters, creatures, spells, items, other notes), by simple reference or inline

## UI & Polish

> Cross-cutting interface work, independent of the phases above.

- [x] Sort the Compendium lists alphabetically (#247)
- [x] Sort the filter values alphabetically (#248)
- [ ] Sort localized labels with a real locale collator (#210)
- [ ] Let the user order a collection by name or by when its entries were added
- [ ] Harmonize the detail pages and their Markdown rendering (oversized titles)
- [ ] Compendium detail: revisit how the type and sub-type are presented (#198, #200)
- [ ] Browse from one item to the next by swiping through a list
- [ ] Compendium detail: switch the entity's language on the fly (e.g. Fireball ↔ Boule de feu), app locale unchanged - [ADR-001](adr/adr-001-data-model.md)

## Online & Sharing

> Features that need the online backend. Once it is chosen (first item), each feature lands, where it has one, once its local counterpart exists - [PRD-000](prd/prd-000-vision.md)

- [ ] Choose the online backend: a self-hosted Ktor server with its own database, or a managed one - ADR to write, see [ADR-008](adr/adr-008-ktor-server.md)
- [ ] User accounts (registration and login) - [PRD-000](prd/prd-000-vision.md)
- [ ] Develop the sync engine on which will rely all sync logic and conflict resolution
- [ ] Sync collections and custom entries with backend
- [ ] Sync character sheets with backend
- [ ] Sync campaigns with backend
- [ ] Sync notes with backend
- [ ] Campaign roles (GM, Co-GM, Player) - [PRD-003](prd/prd-003-campaigns.md)
- [ ] Share campaigns with other users - [PRD-003](prd/prd-003-campaigns.md)
- [ ] Share character sheets with campaign members - [PRD-002](prd/prd-002-character-sheet.md)
- [ ] GM assigns items to a player's inventory - [PRD-003](prd/prd-003-campaigns.md)
- [ ] Note visibility / Secret folders (GM vs. players) - [PRD-003](prd/prd-003-campaigns.md)
- [ ] Share custom compendium entries within a group of players - PRD to write; reopens the served compendium question ([PRD-000](prd/prd-000-vision.md))

## Standalone Features

> Features outside the numbered phases; each item states its own dependencies.

- [x] Dice roller - [PRD-006](prd/prd-006-dice.md)
- [ ] Create custom compendium entries (spells, items, creatures) - [PRD-001](prd/prd-001-reference-data.md)
- [ ] Export and import compendium entries (spells, items, creatures) with their full data - PRD to write, likely starting from the bundled `Api*` format ([ADR-008](adr/adr-008-ktor-server.md))
- [ ] Import and export a collection, carrying its entries in full - same PRD, after the entry export; extends the [PRD-007](prd/prd-007-character-import-export.md) envelope
- [ ] Attach creatures from the compendium to a campaign, the same way collections attach to a character - [PRD-003](prd/prd-003-campaigns.md), after the campaign MVP (Phase 6) and the collections attached to the character
- [ ] Back up and restore the full app state (all user data and preferences) - PRD to write, after the collection export
- [ ] Generator gallery (curated tables) - [PRD-005](prd/prd-005-generators.md)
- [ ] AI-based generators - feasibility study required - [PRD-005](prd/prd-005-generators.md)

## Exploration

> Ideas to qualify before they become phases.

- [ ] Split the app into focused mini apps, each covering one subset (spells, items, creatures, characters)
  - [ ] Qualify a standalone dice roller app - [PRD-006](prd/prd-006-dice.md), Open Questions

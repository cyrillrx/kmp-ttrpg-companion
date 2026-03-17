# Roadmap

This document tracks the macro vision and planned phases for the TTRPG Companion app.
For detailed feature specifications, see the [PRDs](prd/).

## Phase 0 — Cleanup and Revamp the `HomeScreen`

> Resolve open design decisions before moving forward

- [ ] Decide on Spellbook layout (vertical list vs. horizontal cards) and remove the alternative
- [ ] Remove the duplicate Spellbook button from `HomeScreen`

## Phase 1 — Consolidate existing features: [PRD-001](prd/PRD-001-REFERENCE-DATA.md)

> Bring current implementations to a production-ready state with functional browsing and filtering.

- [ ] Complete Spellbook filters (school, class) — [PRD-001a](prd/PRD-001a-SPELLBOOK.md)
- [ ] Complete Inventory filters (type, rarity) — [PRD-001b](prd/PRD-001b-ITEM-LIST.md)
- [ ] Complete Bestiary filters (type, CR) — [PRD-001c](prd/PRD-001c-BESTIARY.md)

## Phase 2 — Character Sheets

> Allow users to create and manage character sheets.

- [ ] Persist `PlayerCharacter` with SQLDelight (currently RAM only)
- [ ] Create, list and edit character sheets — [PRD-002](prd/PRD-002-CHARACTER-SHEET.md)

## Phase 3 — Campaign integration

> Connect entities together through campaigns. [PRD-003](prd/PRD-003-CAMPAIGNS.md)

- [ ] Create, list and edit campaigns
- [ ] Campaign roles (GM, Co-GM, Player)
- [ ] Share campaigns with other users

## Phase 4 — Local lists

> Allow users to build and manage their own lists. [PRD-001](prd/PRD-001-REFERENCE-DATA.md)

- [ ] Local spell lists — [PRD-001a](prd/PRD-001a-SPELLBOOK.md)
- [ ] Local item lists — [PRD-001b](prd/PRD-001b-ITEM-LIST.md)
- [ ] Local bestiary lists — [PRD-001c](prd/PRD-001c-BESTIARY.md)

## Phase 5 — New features

> Independent features that can be developed in parallel.

- [ ] Create Notes — [PRD-004](prd/PRD-004-NOTES.md)
- [ ] Attach notes to campaigns
- [ ] Generator gallery (curated tables) — [PRD-005](prd/PRD-005-GENERATORS.md)
- [ ] Dice roller — [PRD-006](prd/PRD-006-DICE.md)

## Out of scope (for now)

- LLM-based generators — feasibility study required ([PRD-005](prd/PRD-005-GENERATORS.md))
- Cross-entity references in notes ([PRD-004](prd/PRD-004-NOTES.md))
- Secret folders in campaigns ([PRD-003](prd/PRD-003-CAMPAIGNS.md))
- Online public reference data sync ([PRD-001](prd/PRD-001-REFERENCE-DATA.md))

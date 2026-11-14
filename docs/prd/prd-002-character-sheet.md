# PRD-002 — Character Sheet

> **Status**: Draft | **Version**: 0.5 | **Last updated**: 2026-11-13

## Overview

The Character Sheet module allows users to create, view, edit, and delete character sheets for both Player Characters (PCs) and Non-Player Characters (NPCs). It serves as the central record for a character's identity, stats, abilities, and inventory.

PCs and NPCs use the **same `Character` model**. An NPC is a `Character` with `Class.UNKNOWN` and an arbitrary level — no separate subtype. Campaign roles (GM controls NPC, player controls PC) are handled at the campaign layer, not in the character model (see [ADR-003](../adr/adr-003-character-model.md)).

At this stage, character sheets live in the user's personal library and are independent of any campaign.
Attaching sheets to a campaign is part of the local campaign MVP in [PRD-003: Campaigns](prd-003-campaigns.md); sharing with other players and access control come with the online features.

## Goals

- Provide a complete digital character sheet that replaces paper sheets.
- Support both PCs and NPCs in the same personal library.
- Work fully offline.

## User Stories

| As a… | I want to…                               | So that…                                                 |
|-------|------------------------------------------|----------------------------------------------------------|
| User  | Create a new character sheet (PC or NPC) | I have a digital record for my character                 |
| User  | Edit my character sheet at any time      | I can update stats, HP, inventory, etc. during a session |
| User  | Delete a character sheet I own           | I can remove characters I no longer use                  |
| User  | Roll a skill, a save or the initiative   | I get the total without adding the bonus myself          |

**Phase 3 — Campaign integration (see PRD-003)**

| As a… | I want to…                                         | So that…                             |
|-------|----------------------------------------------------|--------------------------------------|
| GM    | Attach a character sheet to a campaign I own       | My campaign has a full roster        |
| User  | View character sheets shared with me in a campaign | I can see relevant party or NPC info |

## Functional Requirements

### Phase 1 — MVP (Offline)

**General Info**
- [x] Character name
- [x] Race / Ancestry
- [x] Class(es) and level
- [x] Background
- [x] Alignment

**Ability Scores & Modifiers**
- [x] The six core stats: Strength, Dexterity, Constitution, Intelligence, Wisdom, Charisma
- [x] Auto-calculated modifiers from scores

**Combat Stats**
- [x] Armor Class (AC)
- [x] Initiative (calculated from DEX modifier by default; trait/feat bonuses handled in a later phase)
- [x] Speed
- [x] Current HP, Maximum HP, Temporary HP
- [ ] Hit Dice (calculated from `level + class.hitDie`, not stored — see [ADR-001 §9](../adr/adr-001-data-model.md))

**Skills & Proficiencies**
- [x] Saving throws (with proficiency toggle)
- [x] Skill list with proficiency and expertise toggles
- [ ] Passive Perception (auto-calculated from WIS modifier by default; trait/feat bonuses handled in a later phase)
- [ ] Proficiency bonus (auto-calculated from level)
- [ ] Languages and other proficiencies

**Rolling from the sheet**
- [x] Tapping a skill, a saving throw or the initiative rolls a d20 with its modifier; the roll is recorded in the dice history with the character and the check (see [PRD-006](prd-006-dice.md) and [ADR-007](../adr/adr-007-roll-context.md)).
- [x] The result shows in a sheet over the character: the check and its bonus, the dice, and the d20, Advantage and Disadvantage actions to roll it again.
- [x] Skills are edited from a pencil next to the section title, since tapping a skill rolls it.
- [ ] Raw ability checks: tapping an ability card still edits its score.
- [ ] Attacks and cantrips: show the dice to roll at the table, then roll them virtually.

**Features & Traits**
- [ ] Class features
- [ ] Racial traits
- [ ] Background feature
- [ ] Feats

**Inventory**
- _Items come from the compendium, through the collections attached to the character (PRD to write), possibly wrapped with sheet-specific data such as quantity and weight. Gear the compendium lacks (rope, rations) waits for custom entries ([PRD-001](prd-001-reference-data.md))_
- [ ] Currency (CP, SP, EP, GP, PP)
- [ ] Carrying capacity (auto-calculated from Strength)

**Spells (if applicable)**
- [ ] Spellcasting ability, spell save DC, spell attack bonus
- [ ] Spell slots (per level, current/max)
- _Prepared and known spells come from the compendium, through the collections attached to the character (PRD to write); homebrew spells wait for custom entries ([PRD-001](prd-001-reference-data.md))_

**Notes**
- [ ] Free-text field for backstory, session notes, roleplaying notes

### Phase 3 — Online with Sync

- [ ] Sync character sheets across devices.
- [ ] Share a character sheet with other campaign members (see PRD-003).

## Non-Functional Requirements

- Edits must be auto-saved locally to prevent data loss.
- The sheet must be usable on small screens (mobile) without horizontal scrolling.
- Auto-calculated fields (modifier, proficiency bonus, passive perception, etc.) must update immediately on input change.

## Out of Scope

- Automated level-up wizard (choosing spells, features) — may be addressed in a future PRD.
- Combat tracker / initiative order — may be addressed in a future PRD.
- Support for non-D&D 5e character sheet formats in this iteration.

## Open Questions

All open questions from v0.1 have been addressed in [ADR-003 — Character Model](../adr/adr-003-character-model.md).

# PRD-002 — Character Sheet

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-03-15

## Overview

The Character Sheet module allows users to create, view, edit, and delete character sheets for both Player Characters (PCs) and Non-Player Characters (NPCs). It serves as the central record for a character's identity, stats, abilities, and inventory.

At this stage, character sheets live in the user's personal library and are independent of any campaign.
Campaign integration (attaching sheets to a campaign, sharing with other players, access control) is a later phase covered in [PRD-003 — Campaigns](PRD-003-CAMPAIGNS.md).

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

**Phase 3 — Campaign integration (see PRD-003)**

| As a… | I want to…                                         | So that…                             |
|-------|----------------------------------------------------|--------------------------------------|
| GM    | Attach a character sheet to a campaign I own       | My campaign has a full roster        |
| User  | View character sheets shared with me in a campaign | I can see relevant party or NPC info |

## Functional Requirements

### Phase 1 — MVP (Offline)

**General Info**
- Character name, player name
- Race / Ancestry
- Class(es) and level
- Background
- Alignment

**Ability Scores & Modifiers**
- The six core stats: Strength, Dexterity, Constitution, Intelligence, Wisdom, Charisma
- Auto-calculated modifiers from scores

**Combat Stats**
- Armor Class (AC)
- Initiative
- Speed
- Current HP, Maximum HP, Temporary HP
- Hit Dice

**Skills & Proficiencies**
- Saving throws (with proficiency toggle)
- Skill list with proficiency and expertise toggles
- Passive Perception (auto-calculated)
- Proficiency bonus (auto-calculated from level)
- Languages and other proficiencies

**Features & Traits**
- Class features
- Racial traits
- Background feature
- Feats

**Inventory**
- List of items as free-form text entries (name only)
- _Linking items from the Item module to a character sheet is a later-phase feature_
- Currency (CP, SP, EP, GP, PP)
- Carrying capacity (auto-calculated from Strength)

**Spells (if applicable)**
- Spellcasting ability, spell save DC, spell attack bonus
- Spell slots (per level, current/max)
- Prepared/known spells as free-form text entries (name only)
- _Linking spells from the Spellbook module to a character sheet is a later-phase feature_

**Notes**
- Free-text field for backstory, session notes, roleplaying notes

### Phase 3 — Online with Sync

- Sync character sheets across devices.
- Attach a character sheet to a campaign and share it with other campaign members (see PRD-003).

## Non-Functional Requirements

- Edits must be auto-saved locally to prevent data loss.
- The sheet must be usable on small screens (mobile) without horizontal scrolling.
- Auto-calculated fields (modifier, proficiency bonus, passive perception, etc.) must update immediately on input change.

## Out of Scope

- Automated level-up wizard (choosing spells, features) — may be addressed in a future PRD.
- Combat tracker / initiative order — may be addressed in a future PRD.
- Support for non-D&D 5e character sheet formats in this iteration.

## Open Questions

- Should NPCs use the same sheet format as PCs, or a simplified stat block format?
